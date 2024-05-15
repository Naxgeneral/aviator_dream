package net.conczin.man_of_many_planes.items;

import immersive_aircraft.item.AircraftItem;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.DyeableLeatherItem;

public class DyeableAircraftItem extends AircraftItem implements DyeableLeatherItem
{
	public DyeableAircraftItem(Properties settings, AircraftConstructor constructor) {
		super(settings, constructor);

		CauldronInteraction.WATER.put(this, CauldronInteraction.DYED_ITEM);
	}
}
