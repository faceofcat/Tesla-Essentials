package com.trials.modsquad.block.container;

import com.trials.modsquad.block.tile.TileGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ContainerGrinder extends Container {

    private TileGrinder grinder;

    public ContainerGrinder(InventoryPlayer inv, TileGrinder grinder){
        this.grinder = grinder;
        addSlotToContainer(new SlotItemHandler(grinder, 0, 56, 35));
        addSlotToContainer(new SlotItemHandler(grinder, 1, 116, 35));

        for(int i = 0; i<3; ++i) for(int j = 0; j<9; ++j) addSlotToContainer(new Slot(inv, j+i*9+9, 8+j*18, 84+i*18));
        for(int i = 0; i<9; ++i) addSlotToContainer(new Slot(inv, i, 8+i*18, 142));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack i = null;
        Slot s = inventorySlots.get(index);
        if(s!=null && s.getHasStack() && s.getStack()!=null){
            ItemStack is = s.getStack();
            i = is.copy();
            if(index<grinder.getSlots()){
                if(!mergeItemStack(is, grinder.getSlots(), 36+grinder.getSlots(), true)) return null;
            }
            else if(!mergeItemStack(is, 0, grinder.getSlots(), false)) return null;
            if(is.stackSize == 0) s.putStack(null);
            else s.onSlotChanged();
            if(is.stackSize == i.stackSize) return null;
            s.onPickupFromSlot(playerIn, is);
        }
        return i;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) { return grinder.getDistanceSq(playerIn.posX+.5, playerIn.posY+.5, playerIn.posZ+.5)<64; }
}
