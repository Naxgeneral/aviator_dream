package net.conczin.man_of_many_planes.fabric;

import net.conczin.man_of_many_planes.ManOfManyPlanesClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class ManOfManyPlanesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ManOfManyPlanesClient.init();
        ManOfManyPlanesClient.ITEM_COLORS.forEach((item, itemColor) -> ColorProviderRegistry.ITEM.register(itemColor, item));
    }
}
