package com.trials.modsquad.Recipies;


import net.minecraft.item.ItemStack;

/**
 * Created by Tjeltigre on 8/4/2016.
 */

public class GrinderRecipe {
    private ItemStack input;
    private ItemStack output;

    GrinderRecipe(ItemStack in, ItemStack out){
        input = in;
        output = out;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

}
