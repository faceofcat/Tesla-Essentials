package com.trials.modsquad.block;

import com.trials.modsquad.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModIngotBlock extends Block {

    public ModIngotBlock(String unlocalizedName, String registryName) {
        super(Material.IRON);
        this.setCreativeTab(Ref.tabModSquad);
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setHarvestLevel("pickaxe", 1);
        setResistance(15F);
        setHardness(3);
    }

}
