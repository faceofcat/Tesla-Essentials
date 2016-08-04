package com.trials.modsquad.Recipies;

import net.minecraft.item.ItemStack;
import java.util.ArrayList;

/**
 * Created by Tjeltigre on 8/4/2016.
 */
public class TeslaRegistry{
    public static TeslaCraftingHandler teslaRegistry = new TeslaCraftingHandler();

    public static void registerGrinderCrafting()
    {

    }
}

class TeslaCraftingHandler {
    private ArrayList<GrinderRecipe> grinderRecipeList = new ArrayList<GrinderRecipe>();

    TeslaCraftingHandler(){
        grinderRecipeList.trimToSize();
    }

    public void registerGrinderRecipe(ItemStack in, ItemStack out)
    {
        this.grinderRecipeList.add(new GrinderRecipe(in, out));
    }

    private ItemStack getGrinderResult(int index)
    {
        return grinderRecipeList.get(index).getOutput();
    }

    private ItemStack getGrinderInput(int index)
    {
        return grinderRecipeList.get(index).getInput();
    }

    @Deprecated
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
}

