package com.trials.modsquad.block.containers;

import com.trials.modsquad.block.TileEntities.TileGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ContainerGrinder extends Container {

    private TileGrinder grinder;

    public ContainerGrinder(InventoryPlayer inv, TileGrinder grinder){
        this.grinder = grinder;
        addSlotToContainer(new Slot(grinder, 0, 56, 35)); // Nice and centered :P
        addSlotToContainer(new Slot(grinder, 1, 116, 35));

        for(int i = 0; i<3; ++i) for(int j = 0; j<9; ++j) addSlotToContainer(new Slot(inv, j+i*9+9, 8+j*18, 84+i*18));
        for(int i = 0; i<9; ++i) addSlotToContainer(new Slot(inv, i, 8+i*18, 142));
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack i = null;
        Slot s = inventorySlots.get(index);
        if(s!=null && s.getHasStack()){
            ItemStack is = s.getStack();
            i = is.copy();
            if(index<grinder.getSizeInventory()){
                if(!mergeItemStack(is, grinder.getSizeInventory(), 36+grinder.getSizeInventory(), true)) return null;
            }
            else if(!mergeItemStack(is, 0, grinder.getSizeInventory(), false)) return null;
            if(is.stackSize == 0) s.putStack(null);
            else s.onSlotChanged();
            if(is.stackSize == i.stackSize) return null;
            s.onPickupFromSlot(playerIn, is);
        }
        return i;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return grinder.isUseableByPlayer(playerIn);
    }
}
