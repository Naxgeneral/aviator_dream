package net.conczin.man_of_many_planes;

import immersive_aircraft.cobalt.registration.Registration;
import net.conczin.man_of_many_planes.client.EconomyPlaneEntityRenderer;
import net.conczin.man_of_many_planes.client.ScarletBiplaneEntityRenderer;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;

import java.util.HashMap;

public class ManOfManyPlanesClient {
    public static void init() {
        Registration.register(ManOfManyPlanes.SCARLET_BIPLANE_ENTITY.get(), ScarletBiplaneEntityRenderer::new);
        Registration.register(ManOfManyPlanes.ECONOMY_PLANE_ENTITY.get(), EconomyPlaneEntityRenderer::new);
    }

    public static HashMap<Item, ItemColor> ITEM_COLORS = new HashMap<>()
    {{
       put(ManOfManyPlanes.SCARLET_BIPLANE_ITEM.get(), getDyeColor(0xEF2323));
       put(ManOfManyPlanes.ECONOMY_PLANE_ITEM.get(), getDyeColor(0xFFFFFF));
    }};

    private static ItemColor getDyeColor(int defaultColor)
    {
        return (item, layer) -> layer != 0 ? -1 : item.getItem() instanceof DyeableLeatherItem dyeable && dyeable.hasCustomColor(item) ? dyeable.getColor(item) : defaultColor;
    }
}
