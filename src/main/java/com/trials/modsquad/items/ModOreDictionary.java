package com.trials.modsquad.items;

import com.trials.modsquad.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionary {

    public static void registerBlocks() {
        register("oreCopper", stackBlock(ModBlocks.oreCopper));
        register("oreTin", stackBlock(ModBlocks.oreTin));
        register("oreLead", stackBlock(ModBlocks.oreLead));

    }

    public static void registerItems() {
        register("ingotCopper", stackItem(ModItems.ingotCopper));
        register("ingotTin", stackItem(ModItems.ingotTin));
        register("ingotLead", stackItem(ModItems.ingotLead));

        register("dustCopper", stackItem(ModItems.dustCopper));
        register("dustTin", stackItem(ModItems.dustTin));
        register("dustLead", stackItem(ModItems.dustLead));
    }

    private static void register(String name, ItemStack stack) {
        OreDictionary.registerOre(name, stack);
    }

    private static ItemStack stackBlock(Block block) {
        return new ItemStack(block);
    }

    private static ItemStack stackItem(Item item) {
        return new ItemStack(item);
    }
}
