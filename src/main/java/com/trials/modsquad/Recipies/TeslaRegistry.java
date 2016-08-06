package com.trials.modsquad.Recipies;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;

/**
 * Created by Tjeltigre on 8/4/2016.
 */
public class TeslaRegistry{
    public static TeslaCraftingHandler teslaRegistry = new TeslaCraftingHandler();

    public static void traditionalCraftingRegister(){

    }

    public static void registerGrinderCrafting()
    {
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT), 2);
    }
}
