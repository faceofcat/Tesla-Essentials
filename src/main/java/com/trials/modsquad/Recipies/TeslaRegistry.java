package com.trials.modsquad.Recipies;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

/**
 * Created by Tjeltigre on 8/4/2016.
 */
public class TeslaRegistry{
    public static TeslaCraftingHandler teslaRegistry = new TeslaCraftingHandler();

    public static void traditionalCraftingRegister(){
        GameRegistry.addSmelting(ModBlocks.oreCopper, new ItemStack(ModItems.ingotCopper), 0F);
        GameRegistry.addSmelting(ModBlocks.oreLead, new ItemStack(ModItems.ingotLead), 0F);
        GameRegistry.addSmelting(ModBlocks.oreTin, new ItemStack(ModItems.ingotTin), 0F);
        GameRegistry.addSmelting(ModItems.dustCopper, new ItemStack(ModItems.ingotCopper), 0F);
        GameRegistry.addSmelting(ModItems.dustLead, new ItemStack(ModItems.ingotLead), 0F);
        GameRegistry.addSmelting(ModItems.dustTin, new ItemStack(ModItems.ingotTin), 0F);
    }

    public static void registerGrinderCrafting()
    {
        teslaRegistry.registerGrinderRecipe(new ItemStack(ModBlocks.oreLead), new ItemStack(ModItems.dustLead), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(ModBlocks.oreTin), new ItemStack(ModItems.dustTin), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(ModBlocks.oreCopper), new ItemStack(ModItems.dustCopper), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.GOLD_ORE), new ItemStack(Items.GOLD_INGOT), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.COAL_ORE), new ItemStack(Items.COAL), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.DIAMOND_ORE), new ItemStack(Items.DIAMOND), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(Items.EMERALD), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE), 12);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Items.DYE, 1, 4), 9);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.WOOL), new ItemStack(Items.STRING), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.CARPET), new ItemStack(Items.STRING), 1);
    }
}
