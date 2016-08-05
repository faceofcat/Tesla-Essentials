package com.trials.modsquad.block.containers;

import com.trials.modsquad.block.TileEntities.TileGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;

public class ContainerGrinder extends Container {

    private TileGrinder grinder;
    private int workTime;

    public ContainerGrinder(InventoryPlayer inv, TileGrinder grinder){
        this.grinder = grinder;
        addSlotToContainer(new Slot(grinder, 0, 56, 35)); // Nice and centered :P
        addSlotToContainer(new Slot(grinder, 1, 116, 35){
            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return false;
            }
        });

        for(int i = 0; i<3; ++i) for(int j = 0; j<9; ++j) addSlotToContainer(new Slot(inv, j+i*9+9, 8+j*18, 84+i*18));
        for(int i = 0; i<9; ++i) addSlotToContainer(new Slot(inv, i, 8+i*18, 142));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener l : listeners) if (workTime != grinder.getField(0)) l.sendProgressBarUpdate(this, 0, grinder.getField(0));

        workTime = grinder.getField(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        grinder.setField(id, data);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        System.out.println("Transferring "+index);
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
