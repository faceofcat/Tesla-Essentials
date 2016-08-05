package com.trials.modsquad.block.containers;

import com.trials.modsquad.block.TileEntities.TileFurnaceGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;

public class ContainerFurnaceGenerator extends Container {

    private TileFurnaceGenerator generator;
    private int workTime;

    public ContainerFurnaceGenerator(InventoryPlayer inv, TileFurnaceGenerator generator){
        this.generator = generator;
        addSlotToContainer(new SlotFurnaceFuel(generator, 0, 80, 34)); // Nice and centered :P

        for(int i = 0; i<3; ++i) for(int j = 0; j<9; ++j) addSlotToContainer(new Slot(inv, j+i*9+9, 8+j*18, 84+i*18));
        for(int i = 0; i<9; ++i) addSlotToContainer(new Slot(inv, i, 8+i*18, 142));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener l : listeners) if (workTime != generator.getField(0)) l.sendProgressBarUpdate(this, 0, generator.getField(0));

        workTime = generator.getField(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        generator.setField(id, data);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        System.out.println("Transferring "+index);
        ItemStack i = null;
        Slot s = inventorySlots.get(index);
        if(s!=null && s.getHasStack()){
            ItemStack is = s.getStack();
            assert is!=null;
            i = is.copy();
            if(index<generator.getSizeInventory()){
                if(!mergeItemStack(is, generator.getSizeInventory(), 36+generator.getSizeInventory(), true)) return null;
            }
            else if(!mergeItemStack(is, 0, generator.getSizeInventory(), false)) return null;
            if(is.stackSize == 0) s.putStack(null);
            else s.onSlotChanged();
            if(is.stackSize == i.stackSize) return null;
            s.onPickupFromSlot(playerIn, is);
        }
        return i;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return generator.isUseableByPlayer(playerIn);
    }
}
