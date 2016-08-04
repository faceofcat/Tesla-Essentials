package com.trials.modsquad.block.machines;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.TileEntities.TileFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockElectricFurnace extends BlockGrinder {

    public BlockElectricFurnace(String s, String s1) {
        super(s, s1);
        setCreativeTab(Ref.tabModSquad);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFurnace();
    }
}
