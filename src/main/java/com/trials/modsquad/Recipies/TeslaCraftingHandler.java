package com.trials.modsquad.Recipies;

import net.minecraft.item.ItemStack;
import java.util.ArrayList;

public class TeslaCraftingHandler {
    private ArrayList<GrinderRecipe> grinderRecipeList = new ArrayList<>();

    TeslaCraftingHandler(){
        grinderRecipeList.trimToSize();
    }

    public void registerGrinderRecipe(ItemStack in, ItemStack out, int amount)
    {
        this.grinderRecipeList.add(new GrinderRecipe(in, out, amount));
    }

    private ItemStack getGrinderResult(int index)
    {
        return grinderRecipeList.get(index).getOutput();
    }

    private ItemStack getGrinderInput(int index)
    {
        return grinderRecipeList.get(index).getInput();
    }

    public int getGrinderRecipeIndex(ItemStack in)
    {
        for(int i = 0; i< grinderRecipeList.size(); i++)
        {
            if(in.isItemEqual(getGrinderInput(i)))
            {
                return i;
            }
        }
        return -1;
    }

    public ItemStack getGrinderOutFromIn(ItemStack in)
    {
        int i = getGrinderRecipeIndex(in);
        if(i > -1)
            return getGrinderResult(i);
        return null;
    }

    public GrinderRecipe getGrinderRecipeFromIn(ItemStack in)
    {
        int i = getGrinderRecipeIndex(in);
        if(i > -1)
            return grinderRecipeList.get(i);
        return null;
    }
}
