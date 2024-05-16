package net.conczin.man_of_many_planes.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.AirplaneEntity;
import immersive_aircraft.entity.misc.Trail;
import net.conczin.man_of_many_planes.ManOfManyPlanes;
import net.conczin.man_of_many_planes.client.ColorUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.List;

public class ScarletBiplaneEntity extends AirplaneEntity {
    public ScarletBiplaneEntity(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world, true);
    }

    @Override
    public Item asItem() {
        return ManOfManyPlanes.SCARLET_BIPLANE_ITEM.get();
    }

    private final List<Trail> trails = List.of(new Trail(40), new Trail(40), new Trail(40), new Trail(40));

    @Override
    public List<Trail> getTrails() {
        return trails;
    }

    protected void trail(Matrix4f transform, int index, float x, float y, float z) {
        Vector4f p0 = transformPosition(transform, x, y - 0.15f, z);
        Vector4f p1 = transformPosition(transform, x, y + 0.15f, z);

        float trailStrength = Math.max(0.0f, Math.min(1.0f, (float) (Math.sqrt(getDeltaMovement().length()) * (0.5f + (pressingInterpolatedX.getSmooth() * x) * 0.025f) - 0.25f)));
        getTrails().get(index).add(p0, p1, trailStrength);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            if (isWithinParticleRange()) {
                Matrix4f transform = getVehicleTransform();
                Matrix3f normalTransform = getVehicleNormalTransform();

                // Trails
                trail(transform, 0, -4.25f, 1.5f, 0.6f);
                trail(transform, 1, 4.25f, 1.5f, 0.6f);
                trail(transform, 2, -6.25f, 3.75f, 0.6f);
                trail(transform, 3, 6.25f, 3.75f, 0.6f);

                // Smoke
                float power = getEnginePower();
                if (power > 0.05) {
                    Vector4f p = transformPosition(transform, (tickCount % 2 == 0 ? -1.0f : 1.0f), 1.2f, 0.0f);
                    Vector3f vel = transformVector(normalTransform, 0.0f, 0.0f, -0.5f);
                    Vec3 velocity = getDeltaMovement();
                    level().addParticle(ParticleTypes.LARGE_SMOKE, p.x(), p.y(), p.z(), vel.x() + velocity.x, vel.y() + velocity.y, vel.z() + velocity.z);
                }
            } else {
                trails.get(0).add(ZERO_VEC4, ZERO_VEC4, 0.0f);
                trails.get(1).add(ZERO_VEC4, ZERO_VEC4, 0.0f);
            }
        }
    }

    @Override
    public double getZoom() {
        return 8.0;
    }

    //Vehicle Dye Color and Item Name Retention - code by Cibernet
    protected static final EntityDataAccessor<Integer> DYE_COLOR = SynchedEntityData.defineId(ScarletBiplaneEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(DYE_COLOR, -1);
    }

    @Override
    protected void addItemTag(@NotNull CompoundTag tag)
    {
        super.addItemTag(tag);
        if(!tag.contains("display"))
            tag.put("display", new CompoundTag());
        CompoundTag displayTag = tag.getCompound("display");
        if(getDyeColor() >= 0)
            displayTag.putInt("color", getDyeColor());

        if(hasCustomName())
            displayTag.putString("Name", Component.Serializer.toJson(getCustomName()));

    }

    @Override
    protected void readItemTag(@NotNull CompoundTag tag)
    {
        super.readItemTag(tag);

        CompoundTag displayTag = tag.getCompound("display");

        if(displayTag.contains("color", 99))
            setDyeColor(displayTag.getInt("color"));

        if(displayTag.contains("Name", Tag.TAG_STRING))
            setCustomName(Component.Serializer.fromJson(displayTag.getString("Name")));
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        if(tag.contains("Color"))
            setDyeColor(tag.getInt("Color"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putInt("Color", getDyeColor());
    }

    public int getDyeColor()
    {
        return entityData.get(DYE_COLOR);
    }

    public void setDyeColor(int v)
    {
        entityData.set(DYE_COLOR, v);
    }

    public int getBodyColor()
    {
        return getDyeColor() < 0 ? 0xEF2323 : getDyeColor();
    }

    public int getHighlightColor()
    {
        //Gets dye color and separates it into RGB, then turns that into HSB
        int[] rgb = ColorUtils.hexToRGB(getBodyColor());
        float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], null);

        //Multiplies Saturation (hsb[1]) and Brightness (hsb[2]) by a factor
        hsb[1] = Mth.clamp(hsb[1] * 0.88311f, 0, 1);
        hsb[2] = Mth.clamp(hsb[2] * 1.11494f, 0, 1);

        //Turns color back into decimal and returns outcome
        Color resultColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        return resultColor.getRGB();
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }
}
