package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileElectricFurnace extends TileEntity implements IItemHandlerModifiable, ITickable{

    private ItemStack[] inventory;
    private BaseTeslaContainer container;
    private boolean isSmelting = false;
    private int workTime = 0;

    public TileElectricFurnace(){
        container = new BaseTeslaContainer();
        inventory = new ItemStack[2];
    }

    @Override
    public void update() {

    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack;
    }

    @Override
    public int getSlots() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack tmp;
        int val;
        if(inventory[slot]==null || slot==1) return stack;
        if(inventory[slot].isItemEqual(stack)){
            if(inventory[slot].stackSize+stack.stackSize<=64 && inventory[slot].stackSize+stack.stackSize<=stack.getMaxStackSize()) {
                if(!simulate) inventory[slot].stackSize += stack.stackSize;
                return null;
            }
            tmp = stack.copy();
            tmp.stackSize = stack.getMaxStackSize() - inventory[slot].stackSize - stack.stackSize;
            if(!simulate) inventory[slot].stackSize = stack.getMaxStackSize();
            return tmp;
        }
        if(simulate) return inventory[slot];
        tmp = inventory[slot];
        inventory[slot] = stack;
        return tmp;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack split;
        if(inventory[slot] == null || slot==0) return null;
        if(amount>=inventory[slot].stackSize){
            split = inventory[slot];
            if(!simulate) inventory[slot] = null;
            return split;
        }
        if(simulate) (split = inventory[slot].copy()).stackSize = amount;
        else split = inventory[slot].splitStack(amount);
        return split;
    }
}
