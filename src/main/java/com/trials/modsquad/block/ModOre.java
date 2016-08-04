package com.trials.modsquad.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class ModOre extends Block {

    public ModOre(String unlocalizedName, String registryName) {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);

    }

}
