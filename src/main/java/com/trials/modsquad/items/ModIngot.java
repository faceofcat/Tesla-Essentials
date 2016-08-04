package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModIngot extends Item {

    public ModIngot(String unlocalizedName, String registryName) {
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(Ref.tabModSquad);
    }

}
