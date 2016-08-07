package com.trials.modsquad.block.containers;

import com.trials.modsquad.block.TileEntities.TileCapacitor;
import com.trials.modsquad.block.TileEntities.TileCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ContainerCapacitor extends Container {

    private TileCapacitor capacitor;

    public ContainerCapacitor(InventoryPlayer inv, TileCapacitor capacitor){
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return capacitor.getDistanceSq(playerIn.posX+.5, playerIn.posY+.5, playerIn.posZ+.5)<64;
    }
}
