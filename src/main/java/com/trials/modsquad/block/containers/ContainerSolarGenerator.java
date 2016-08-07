package com.trials.modsquad.block.containers;

import com.trials.modsquad.block.TileEntities.TileCapacitor;
import com.trials.modsquad.block.TileEntities.TileSolarPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerSolarGenerator extends Container {

    private TileSolarPanel solarPanel;

    public ContainerSolarGenerator(InventoryPlayer inv, TileSolarPanel solarPanel){
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return solarPanel.getDistanceSq(playerIn.posX+.5, playerIn.posY+.5, playerIn.posZ+.5)<64;
    }
}
