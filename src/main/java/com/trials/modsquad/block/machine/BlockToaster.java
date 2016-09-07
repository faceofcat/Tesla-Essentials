package com.trials.modsquad.block.machine;

import com.trials.modsquad.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockToaster extends Block {

    public BlockToaster(String unlocalizedName, String registryName) {
        super(Material.IRON);
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(Ref.tabModSquad);
    }

    @Override public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(4 * 0.0625, 0 * 0.0625, 1 * 0.0625, 12 * 0.0625, 11 * 0.0625, 15 * 0.0625);
    }

}