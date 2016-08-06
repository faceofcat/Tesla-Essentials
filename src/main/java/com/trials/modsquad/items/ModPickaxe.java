package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.minecraft.item.ItemPickaxe;

public class ModPickaxe extends ItemPickaxe {

    public ModPickaxe(String unlocalizedName, String registryName, ToolMaterial material) {
        super(material);
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(Ref.tabModSquad);
    }

}
