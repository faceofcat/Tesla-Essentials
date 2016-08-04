package com.trials.modsquad.items;

import com.trials.modsquad.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionary {

    public void registerBlocks() {
        register("oreCopper", stack(ModBlocks.oreCopper));
        register("oreTin", stack(ModBlocks.oreTin));
        register("oreSilver", stack(ModBlocks.oreSilver));
        register("oreLead", stack(ModBlocks.oreLead));

    }

    public static void register(String name, ItemStack stack) {
        OreDictionary.registerOre(name, stack);
    }

    public ItemStack stack(Block block) {
        ItemStack stack = new ItemStack(block);
        return stack;
    }
}
