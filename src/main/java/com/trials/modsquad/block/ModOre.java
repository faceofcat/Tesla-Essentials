package com.trials.modsquad.block;

import com.trials.modsquad.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.oredict.OreDictionary;

public class ModOre extends Block {

    public ModOre(String unlocalizedName, String registryName, int level) {
        super(Material.ROCK);
        this.setCreativeTab(Ref.tabModSquad);
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setHarvestLevel("pickaxe", level);
        setResistance(15F);
        setHardness(3);
    }

}
