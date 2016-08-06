package com.trials.modsquad.block.ores;

import com.trials.modsquad.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class OreBase extends Block {
    public OreBase(String unloc, String reg) {
        super(Material.ROCK);
        setUnlocalizedName(unloc);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
        setHarvestLevel("pickaxe", 1);
    }
}
