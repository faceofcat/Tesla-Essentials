package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.TabCompleter;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;


public class TileCharger extends TileEntity implements IItemHandlerModifiable, ITickable {


    private ItemStack[] inventory;
    private BaseTeslaContainer container;

    public TileCharger(){
        container = new BaseTeslaContainer();
        inventory = new ItemStack[1];
    }


    @Override
    public int getSlots() {
        return inventory.length;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack s = inventory[slot];
        if(simulate) return s;
        inventory[slot] = stack;
        return s;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(simulate) return inventory[slot];
        ItemStack s = inventory[slot];
        if(s.stackSize-amount<=0) inventory[slot]=null;
        else inventory[slot] = s.splitStack(amount);
        return s;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER) return (T) container; // Not picky
        if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this; // Not picky
        return super.getCapability(capability, facing); // Possibly picky
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound c = super.serializeNBT();
        System.out.println("Serializing");
        try{
            Field f = NBTTagCompound.class.getDeclaredField("tagMap");
            f.setAccessible(true);
            Map<String, NBTBase> r = (Map<String, NBTBase>) f.get(c);
            Map<String, NBTBase> m = (Map<String, NBTBase>) f.get(container);
            for(String s : m.keySet()){
                System.out.println("Ser: "+s);
                r.put(s, m.get(s)); // Move container tags to my tags
            }
            f.set(c, r);
        }catch(Exception ignored){}
        return c;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        System.out.println("Deserializing");
        super.deserializeNBT(nbt);
        container.deserializeNBT(nbt);
        System.out.println(container.getStoredPower()+" "+nbt.toString());
    }

    @Override
    public void update() {
        if (inventory[0] != null && inventory[0].hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN) &&
                inventory[0].hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN)) {
            ITeslaConsumer c = inventory[0].getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN);
            ITeslaHolder h = inventory[0].getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
            if (h.getStoredPower() < h.getCapacity()) container.takePower(c.givePower(Math.min(container.getOutputRate(), container.getStoredPower()), false), false);
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack!=null?stack.copy():null;
    }
}
