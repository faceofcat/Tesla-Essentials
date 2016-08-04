package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class TileGrinder extends TileEntity implements IInventory, ITeslaConsumer, ITeslaHolder{
    // Primitives
    private final int[] inventory; //Technically not primitive, but is referencing primitives

    // Objects
    private BaseTeslaContainer container;

    public TileGrinder(){
        inventory = new int[2];
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
        return null;
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {

    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
