package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaHolder;
import net.minecraft.item.Item;

public class ItemTest extends Item implements ITeslaHolder {

    private int storedPower = 0;
    private int capacity = 10000;

    public ItemTest() {
        setUnlocalizedName("itemTest");
        setCreativeTab(Ref.tabModSquad);
        setRegistryName("ItemTest");
        setMaxStackSize(1);

    }

    @Override
    public long getStoredPower() {
        return storedPower;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }
}
