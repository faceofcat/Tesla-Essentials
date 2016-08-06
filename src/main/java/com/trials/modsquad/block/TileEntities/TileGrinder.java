package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.Recipies.GrinderRecipe;
import com.trials.modsquad.Recipies.TeslaRegistry;
import com.trials.modsquad.block.ModBlocks;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.trials.modsquad.block.ModBlocks.getRelativeFace;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileGrinder extends TileEntity implements IItemHandlerModifiable, ITickable {
    // Primitives
    private int grindTime;
    public static final int DEFAULT_GRIND_TIME = 60;
    public static final int DRAW_PER_TICK = 10;
    private boolean isGrinding = false;

    // Objects
    private final ItemStack[] inventory; // I'm an idiot
    private BaseTeslaContainer container;
    private ThreadLocalRandom random = ThreadLocalRandom.current();

    public TileGrinder(){
        inventory = new ItemStack[2];
        container = new BaseTeslaContainer();
        container.setInputRate(100);
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
        if(slot==1) return null;
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
        if(inventory[0]==null && isGrinding){
            isGrinding = false;
            grindTime = 0;
        }
        return s;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER)
            return (T) container;
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound c = super.serializeNBT();
        try{
            Field f = NBTTagCompound.class.getDeclaredField("tagMap");
            f.setAccessible(true);
            Map<String, NBTBase> r = (Map<String, NBTBase>) f.get(c);
            Map<String, NBTBase> m = (Map<String, NBTBase>) f.get(container);
            for(String s : m.keySet()) r.put(s, m.get(s)); // Move container tags to my tags
            f.set(c, r);
        }catch(Exception ignored){}
        return c;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        container.deserializeNBT(nbt);
    }

    @Override
    public void update() {
        if(isGrinding){
            if(grindTime==0){
                ItemStack s = extractItem(0, 1, false);
                if(inventory[1]==null){
                    inventory[1] = TeslaRegistry.teslaRegistry.getGrinderOutFromIn(s);
                    inventory[1].stackSize = TeslaRegistry.teslaRegistry.getGrinderRecipeFromIn(s).getAmount();
                }else
                    inventory[1].stackSize+=TeslaRegistry.teslaRegistry.getGrinderRecipeFromIn(s).getAmount();
                isGrinding = false;
            }
            if(container.getStoredPower()>DRAW_PER_TICK){
                container.takePower(DRAW_PER_TICK, false);
                isGrinding = false;
                grindTime = 0;
                return;
            }
            --grindTime;
        }else if(inventory[0]!=null && TeslaRegistry.teslaRegistry.hasRecipe(inventory[0]) && (inventory[1] == null ||
                inventory[1] == TeslaRegistry.teslaRegistry.getGrinderOutFromIn(inventory[0])) && container.getStoredPower()>0 && inventory[1].stackSize<64){
            isGrinding = true;
            grindTime = DEFAULT_GRIND_TIME;
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack;
    }
}
