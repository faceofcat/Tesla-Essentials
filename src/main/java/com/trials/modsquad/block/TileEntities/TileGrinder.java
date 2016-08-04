package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import scala.xml.dtd.impl.Base;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class TileGrinder extends TileEntity implements IInventory, ITeslaConsumer, ITeslaHolder{
    // Primitives
    private int workTime = 0;

    // Objects
    private final ItemStack[] inventory; // I'm an idiot
    private BaseTeslaContainer container;

    public TileGrinder(){
        inventory = new ItemStack[2];
        container = new BaseTeslaContainer();
    }

    @Override
    public long givePower(long power, boolean simulated) {
        return container.givePower(power ,simulated);
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
    public int getSizeInventory() {
        return inventory.length;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack s = inventory[index];
        if(s!=null){
            if(s.stackSize <= count) setInventorySlotContents(index, null);
            else if((s = s.splitStack(count)).stackSize==0) setInventorySlotContents(index, null);
        }
        return s;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack s = inventory[index];
        inventory[index] = null;
        return s;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        inventory[index] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5) < 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        // NOP
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        // NOP
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        switch (id){
            case 0:
                return workTime;
            case 1:
                return (int) container.getStoredPower();
            case 2:
                return (int) container.getCapacity();
            case 3:
                return (int) container.getInputRate();
            case 4:
                return (int) container.getOutputRate();
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id){
            case 0:
                workTime = value;
                break;
            case 1:
                try{
                    Field f = BaseTeslaContainer.class.getDeclaredField("stored");
                    f.setAccessible(true);
                    f.setLong(container, value);
                }catch(Exception e){}
                break;
            case 2:
                container.setCapacity(value);
                break;
            case 3:
                container.setInputRate(value);
                break;
            case 4:
                container.setOutputRate(value);
        }
    }

    @Override
    public int getFieldCount() {
        return 5; //Update if more stuff is added
    }

    @Override
    public void clear() {
        workTime = 0;
        try{
            Field f = BaseTeslaContainer.class.getDeclaredField("stored");
            f.setAccessible(true);
            f.setLong(container, 0);
        }catch(Exception e){}
        container.setCapacity(0);
        container.setInputRate(0);
        container.setOutputRate(0);

    }

    @Override
    public String getName() {
        return "modsquad.tilegrinder";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
