package com.trials.modsquad.block.container;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.concurrent.ThreadLocalRandom;

public class ContainerGrinder extends BlockContainer {


    protected ContainerGrinder(Material materialIn) {
        super(materialIn);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        TileEntity t = worldIn.getTileEntity(pos);
        if(!(t instanceof IInventory)) return;
        for(int i = 0; i<((IInventory) t).getSizeInventory(); ++i){

        }
    }
}
