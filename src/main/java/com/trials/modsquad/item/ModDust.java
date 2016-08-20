package com.trials.modsquad.item;

import com.trials.modsquad.Ref;
import net.minecraft.item.Item;

public class ModDust extends Item {

    public ModDust(String unlocalizedName, String registryName) {
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(Ref.tabModSquad);
    }

}
