package com.trials.modsquad.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModDust extends Item {

    public ModDust(String unlocalizedName, String registryName) {
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

}
