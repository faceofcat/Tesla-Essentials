package com.trials.modsquad.item;

import com.trials.modsquad.Ref;
import net.minecraft.item.Item;

public class ItemBread extends Item {

    public ItemBread(String name, String reg) {
        setUnlocalizedName(name);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
    }

}
