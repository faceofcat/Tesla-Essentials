package com.trials.modsquad.Recipies;


import net.minecraft.item.ItemStack;

/**
 * Created by Tjeltigre on 8/4/2016.
 */

public class GrinderRecipe {
    private ItemStack input;
    private ItemStack output;
    private int amount;

    GrinderRecipe(ItemStack in, ItemStack out, int num){
        input = in;
        output = out;
        amount = num;
    }

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
