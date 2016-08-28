package com.trials.modsquad.block.container;

import com.trials.modsquad.block.tile.TileFurnaceGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ContainerFurnaceGenerator extends Container {

    private TileFurnaceGenerator generator;

    public ContainerFurnaceGenerator(InventoryPlayer inv, TileFurnaceGenerator generator){
        this.generator = generator;
        addSlotToContainer(new SlotItemHandler(generator, 0, 80, 34){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return SlotFurnaceFuel.isBucket(stack) || TileEntityFurnace.isItemFuel(stack);
            }
        }); // Nice and centered :P

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
            assert is!=null;
            i = is.copy();
            if(index<generator.getSlots()){
                if(!mergeItemStack(is, generator.getSlots(), 36+generator.getSlots(), true)) return null;
            }
            else if(!mergeItemStack(is, 0, generator.getSlots(), false)) return null;
            if(is.stackSize == 0) s.putStack(null);
            else s.onSlotChanged();
            if(is.stackSize == i.stackSize) return null;
            s.onPickupFromSlot(playerIn, is);
        }
        return i;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return generator.getDistanceSq(playerIn.posX+.5, playerIn.posY+.5, playerIn.posZ+.5)<64;
    }
}
