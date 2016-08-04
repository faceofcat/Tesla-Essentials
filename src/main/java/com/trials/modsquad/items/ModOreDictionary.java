package com.trials.modsquad.items;

import com.trials.modsquad.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionary {

    public static void registerBlocks() {
        register("oreCopper", stacker(ModBlocks.oreCopper));
        register("oreTin", stacker(ModBlocks.oreTin));
        register("oreLead", stacker(ModBlocks.oreLead));

    }

    public static void register(String name, ItemStack stack) {
        OreDictionary.registerOre(name, stack);
    }

    public static ItemStack stacker(Block block) {
        ItemStack stack = new ItemStack(block);
        return stack;
    }
}
