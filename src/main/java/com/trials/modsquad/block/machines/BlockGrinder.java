package com.trials.modsquad.block.machines;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGrinder extends BlockContainer{

    protected BlockGrinder(Material materialIn) {
        super(materialIn);
    }

    protected BlockGrinder(Material materialIn, MapColor color) {
        super(materialIn, color);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
