package com.trials.modsquad.recipe;

import net.minecraft.item.ItemStack;

public class GrinderRecipe {
    private ItemStack input;
    private ItemStack output;
    private int amount;

    GrinderRecipe(ItemStack in, ItemStack out, int num){
        input = in;
        output = out;
        amount = num;
    }
    /**
    GrinderRecipe(OreDictionary in, ItemStack out, int num){
        input = in;
        output = out;
        amount = num;
    }*/

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

    public int getAmount() {
        return amount;
    }

}
