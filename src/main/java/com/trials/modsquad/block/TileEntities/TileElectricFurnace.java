package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.Recipies.TeslaRegistry;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandlerModifiable;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileElectricFurnace extends TileEntity implements IItemHandlerModifiable, ITickable{

    private ItemStack[] inventory;
    private BaseTeslaContainer container;
    private boolean isSmelting = false;
    private int workTime = 0;
    static final int DRAW_PER_TICK = 10;

    public TileElectricFurnace(){
        container = new BaseTeslaContainer();
        inventory = new ItemStack[2];
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void update() {

        if(isSmelting){
            if(workTime==0){
                ItemStack s = extractItem(0, 1, false);
                if(s==null){ // Invalid state that for some reason can arise
                    isSmelting = false;
                    workTime = 0;
                    return;
                }
                if(inventory[1]==null){
                    inventory[1] = FurnaceRecipes.instance().getSmeltingResult(s).copy();
                }else inventory[1].stackSize+=FurnaceRecipes.instance().getSmeltingResult(s).stackSize;
                isSmelting = false;
            }
            if(container.getStoredPower()<DRAW_PER_TICK){
                isSmelting = false;
                workTime = 0;
                return;
            }
            container.takePower(DRAW_PER_TICK, false);
            --workTime;
        }else{
            ItemStack itemstack = inventory[0] != null ? FurnaceRecipes.instance().getSmeltingResult(inventory[0]) : null;
            int size;
            if (itemstack != null && (inventory[1] == null || inventory[1].isItemEqual(itemstack)) && (size = (inventory[1] != null ? inventory[1].stackSize : 0) + itemstack.stackSize) <= 64 &&
                    size <= itemstack.getMaxStackSize()) {
                isSmelting = true;
                workTime = TileEntityFurnace.getItemBurnTime(inventory[0]);
            }
        }
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
        if(slot==1) return stack;
        if(inventory[slot] == null){
            inventory[slot] = stack.copy();
            return null;
        }
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
        if(isSmelting){
            isSmelting = false;
            workTime = 0;
        }
        return tmp;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack split;
        if(inventory[slot]==null) return null;
        if(amount>=inventory[slot].stackSize){
            split = inventory[slot];
            if(!simulate) inventory[slot] = null;
            if(slot==0 && isSmelting){
                isSmelting = false;
                workTime = 0;
            }
            return split;
        }
        if(simulate) (split = inventory[slot].copy()).stackSize = amount;
        else split = inventory[slot].splitStack(amount);
        return split;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == CAPABILITY_HOLDER || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_HOLDER || capability==CAPABILITY_PRODUCER) return (T) container;
        return super.getCapability(capability, facing);
    }
}
