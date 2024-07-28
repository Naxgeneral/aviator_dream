package net.conczin.man_of_many_planes;

import immersive_aircraft.ItemColors;
import immersive_aircraft.cobalt.registration.Registration;
import net.conczin.man_of_many_planes.client.EconomyPlaneEntityRenderer;
import net.conczin.man_of_many_planes.client.ScarletBiplaneEntityRenderer;

import static immersive_aircraft.ItemColors.getDyeColor;

public class ManOfManyPlanesClient {
    public static void init() {
        Registration.register(ManOfManyPlanes.SCARLET_BIPLANE_ENTITY.get(), ScarletBiplaneEntityRenderer::new);
        Registration.register(ManOfManyPlanes.ECONOMY_PLANE_ENTITY.get(), EconomyPlaneEntityRenderer::new);
    }

    static {
        ItemColors.ITEM_COLORS.put(ManOfManyPlanes.SCARLET_BIPLANE_ITEM.get(), getDyeColor(0xEF2323));
        ItemColors.ITEM_COLORS.put(ManOfManyPlanes.ECONOMY_PLANE_ITEM.get(), getDyeColor(0xFFFFFF));
    }
}
