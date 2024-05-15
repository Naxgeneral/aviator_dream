package net.conczin.man_of_many_planes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import immersive_aircraft.client.render.entity.renderer.AircraftEntityRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.BBModelRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.resources.bbmodel.BBBone;
import immersive_aircraft.resources.bbmodel.BBFaceContainer;
import immersive_aircraft.resources.bbmodel.BBModel;
import immersive_aircraft.resources.bbmodel.BBObject;
import net.conczin.man_of_many_planes.ManOfManyPlanes;
import net.conczin.man_of_many_planes.entity.ScarletBiplaneEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;

public class ScarletBiplaneEntityRenderer extends AircraftEntityRenderer<ScarletBiplaneEntity> {
    private static final ResourceLocation ID = ManOfManyPlanes.locate("scarlet_biplane");

    private final ModelPartRenderHandler<ScarletBiplaneEntity> model = new ModelPartRenderHandler<ScarletBiplaneEntity>()
            .add("dyed_body", (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) ->
                    renderPartColored(model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer, entity.getBodyColor()))
            .add("dyed_body_highlights", (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) ->
                    renderPartColored(model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer, entity.getHighlightColor()));

    public static  <T extends VehicleEntity> void renderPartColored(BBModel model, BBObject object, MultiBufferSource vertexConsumerProvider, T entity, PoseStack matrixStack, int light, float time, ModelPartRenderHandler<T> modelPartRenderer, int color)
    {
//        float red = ((color & 16711680) >> 16) / 255.0F;
//        float green = ((color & '\uff00') >> 8) / 255.0F;
//        float blue = (color & 255) / 255.0F;
        float alpha = 1;

        float[] rgb = ColorUtils.hexToDecimalRGB(color);

//                BBModelRenderer.renderObject(model, object, matrixStack, vertexConsumerProvider, light, time, entity, modelPartRenderer, red, green, blue, 1);

        if (object instanceof BBFaceContainer cube) {
            BBModelRenderer.renderFaces(cube, matrixStack, vertexConsumerProvider, light, rgb[0], rgb[1], rgb[2], alpha, 1.0f, 1.0f, 0.0f, 0.0f, null);
        } else if (object instanceof BBBone bone) {
            boolean shouldRender = bone.visibility;
            if (bone.name.equals("lod0")) {
                shouldRender = entity.isWithinParticleRange();
            } else if (bone.name.equals("lod1")) {
                shouldRender = !entity.isWithinParticleRange();
            }

            if (shouldRender) {
                bone.children.forEach(child -> BBModelRenderer.renderObject(model, child, matrixStack, vertexConsumerProvider, light, time, entity, modelPartRenderer, rgb[0], rgb[1], rgb[2], alpha));
            }
        }
    }

    @Override
    protected ResourceLocation getModelId() {
        return ID;
    }

    public ScarletBiplaneEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected ModelPartRenderHandler<ScarletBiplaneEntity> getModel(AircraftEntity entity) {
        return model;
    }
}
