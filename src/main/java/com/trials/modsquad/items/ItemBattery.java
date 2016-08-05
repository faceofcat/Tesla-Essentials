package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public class ItemBattery extends Item implements ITeslaHolder {

    private long storedPower;
    private long capacity;

    public ItemBattery(String name, String reg, long storedPower) {
        setUnlocalizedName(name);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
        setMaxStackSize(1);
        this.storedPower = storedPower;
        capacity = 10000;
        setMaxDamage(Integer.parseInt(Long.toString(capacity)));
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {

        super.addInformation(stack, playerIn, tooltip, advanced);

        tooltip.add(I18n.format("Power: " + Long.toString(storedPower) + "/" + Long.toString(capacity)));
    }

    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, NBTTagCompound nbt) {

        return new BaseTeslaContainerProvider(new BaseTeslaContainer());

    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {



        storedPower = stack.getItemDamage() * (-1) + capacity;

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
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
