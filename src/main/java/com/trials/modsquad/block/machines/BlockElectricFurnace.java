package com.trials.modsquad.block.machines;

import com.trials.modsquad.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockElectricFurnace extends Block {

    public BlockElectricFurnace() {
        super(Material.ANVIL);
        setUnlocalizedName(Ref.BlockReference.MACHINEFURNACE.getUnlocalizedName());
        setRegistryName(Ref.BlockReference.MACHINEFURNACE.getRegistryName());
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

}
