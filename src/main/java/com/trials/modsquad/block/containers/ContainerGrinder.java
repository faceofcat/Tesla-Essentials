package com.trials.modsquad.block.containers;

import com.trials.modsquad.block.TileEntities.TileGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

import java.nio.ByteBuffer;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class ContainerGrinder extends Container {

    private TileGrinder grinder;
    private int workTime;

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
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putLong(grinder.getCapability(CAPABILITY_HOLDER, EnumFacing.DOWN).getStoredPower());
        ByteBuffer buf1 = ByteBuffer.wrap(buf.array());
        for(IContainerListener listener : listeners) {
            System.out.println(listener);
            listener.sendProgressBarUpdate(this, 0, buf1.getInt());
            listener.sendProgressBarUpdate(this, 1, buf1.getInt());
        }
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack i = null;
        Slot s = inventorySlots.get(index);
        if(s!=null && s.getHasStack()){
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
