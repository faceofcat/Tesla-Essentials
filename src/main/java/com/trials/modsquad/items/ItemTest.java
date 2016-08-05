package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

import java.util.List;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class ItemTest extends Item implements ITeslaHolder, ITeslaConsumer, ICapabilityProvider {

    private BaseTeslaContainer container;
    private long stored;
    private long capacity;
    private long inputRate;
    private long outputRate;

    public ItemTest() {
        setUnlocalizedName("itemTest");
        setCreativeTab(Ref.tabModSquad);
        setRegistryName("ItemTest");
        setMaxStackSize(1);
        container = new BaseTeslaContainer(1000, 20, 20);

        this.stored = container.getStoredPower();
        this.capacity = container.getCapacity();
        this.inputRate = container.getInputRate();
        this.outputRate = container.getOutputRate();

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

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY_CONSUMER || capability == CAPABILITY_HOLDER;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add("Power: " + getStoredPower() + "/" + getCapacity());
        super.addInformation(stack, playerIn, tooltip, advanced);
    }
}
