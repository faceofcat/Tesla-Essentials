package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.item.Item;

public class ItemTest extends Item implements ITeslaHolder, ITeslaConsumer {

    private BaseTeslaContainer container;

    public ItemTest() {
        setUnlocalizedName("itemTest");
        setCreativeTab(Ref.tabModSquad);
        setRegistryName("ItemTest");
        setMaxStackSize(1);
        container = new BaseTeslaContainer(1000, 20, 20);

    }

    @Override
    public long getStoredPower() {
        return container.getStoredPower();
    }

    @Override
    public long getCapacity() {
        return container.getCapacity();
    }

    @Override
    public long givePower(long power, boolean simulated) {
        return container.givePower(power, simulated);
    }
}
