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
        //Smelting ores
        GameRegistry.addSmelting(ModBlocks.oreCopper, new ItemStack(ModItems.ingotCopper), 0F);
        GameRegistry.addSmelting(ModBlocks.oreLead, new ItemStack(ModItems.ingotLead), 0F);
        GameRegistry.addSmelting(ModBlocks.oreTin, new ItemStack(ModItems.ingotTin), 0F);
        GameRegistry.addSmelting(ModItems.dustCopper, new ItemStack(ModItems.ingotCopper), 0F);
        GameRegistry.addSmelting(ModItems.dustLead, new ItemStack(ModItems.ingotLead), 0F);
        GameRegistry.addSmelting(ModItems.dustTin, new ItemStack(ModItems.ingotTin), 0F);
        GameRegistry.addSmelting(ModItems.dustElectricAlloy, new ItemStack(ModItems.ingotElectricAlloy), 0F);
        //Materials
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.dustElectricAlloy, 2), new ItemStack(ModItems.dustCopper), new ItemStack(ModItems.dustCopper),
                new ItemStack(ModItems.dustTin), new ItemStack(Items.REDSTONE));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockCopper),
                "III",
                "III",
                "III",
                'I', ModItems.ingotCopper
        );
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockTin),
                "III",
                "III",
                "III",
                'I', ModItems.ingotTin
        );
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockLead),
                "III",
                "III",
                "III",
                'I', ModItems.ingotLead
        );
        GameRegistry.addRecipe(new ItemStack(ModItems.electricBoots),
                "I I",
                "I I",
                'I', ModItems.ingotElectricAlloy
        );
        GameRegistry.addRecipe(new ItemStack(ModItems.electricHelmet),
                "III",
                "IGI",
                'I', ModItems.ingotElectricAlloy, 'G', new ItemStack(Blocks.GLASS_PANE, 1, 13)
        );
        GameRegistry.addRecipe(new ItemStack(ModItems.electricLeggings),
                "III",
                "I I",
                "I I",
                'I', ModItems.ingotElectricAlloy
        );
        GameRegistry.addRecipe(new ItemStack(ModItems.electricChestplate),
                "I I",
                "ILI",
                "III",
                'I', ModItems.ingotElectricAlloy, 'L', ModItems.ingotLead
        );
        GameRegistry.addRecipe(new ItemStack(ModItems.jetChestplate),
                "BRB",
                "LCL",
                "SFS",
                'C', ModItems.electricChestplate, 'L', ModItems.ingotLead, 'F', Blocks.FURNACE, 'S', Items.FLINT_AND_STEEL, 'B', Blocks.STONE_BUTTON, 'R', Blocks.UNPOWERED_COMPARATOR);
        GameRegistry.addRecipe(new ItemStack(ModItems.terraSmasher),
                "EEE",
                "TLT",
                " L ",
                'E', ModItems.ingotElectricAlloy, 'L', ModItems.ingotLead, 'T', ModItems.ingotTin
        );
        GameRegistry.addRecipe(new ItemStack(ModBlocks.furnaceGen),
                "  T",
                "CLF",
                "TTT",
                'C', ModItems.ingotCopper, 'L', ModItems.ingotLead, 'T', ModItems.ingotTin, 'F', new ItemStack(Blocks.FURNACE)
        );
        GameRegistry.addRecipe(new ItemStack(ModBlocks.grinder),
                "  T",
                "TLT",
                "I I",
                'I', Items.IRON_INGOT, 'L', ModItems.ingotLead, 'T', ModItems.ingotTin
        );
        GameRegistry.addRecipe(new ItemStack(ModBlocks.charger),
                "TLL",
                "TLT",
                "TTT",
                'L', ModItems.ingotLead, 'T', ModItems.ingotTin
        );
        GameRegistry.addRecipe(new ItemStack(ModBlocks.electricFurnace),
                "TTT",
                "FCF",
                "TTT",
                'T', ModItems.ingotTin, 'F', Blocks.FURNACE, 'C', Blocks.COAL_BLOCK);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.capacitor),
                "TTT",
                "LLL",
                "TTT",
                'T', ModItems.ingotTin, 'L', ModItems.ingotLead);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.leadCable, 8),
                "LRL",
                'L', ModItems.ingotLead, 'W', new ItemStack(Blocks.WOOL, 1, 15), 'R', Items.REDSTONE);
        GameRegistry.addRecipe(new ItemStack(ModItems.poweredPotato),
                " P ",
                "PLP",
                " P ",
                'P', Items.POTATO, 'L', ModItems.ingotLead);
    }

    public static void registerGrinderCrafting()
    {
        //Mod Ores
        teslaRegistry.registerGrinderRecipe(new ItemStack(ModBlocks.oreLead), new ItemStack(ModItems.dustLead), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(ModBlocks.oreTin), new ItemStack(ModItems.dustTin), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(ModBlocks.oreCopper), new ItemStack(ModItems.dustCopper), 2);
        //Vanilla Ores
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.GOLD_ORE), new ItemStack(Items.GOLD_INGOT), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.COAL_ORE), new ItemStack(Items.COAL), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.DIAMOND_ORE), new ItemStack(Items.DIAMOND), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(Items.EMERALD), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE), 12);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Items.DYE, 1, 4), 9);
        //Misc
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.WOOL), new ItemStack(Items.STRING), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.CARPET), new ItemStack(Items.STRING), 1);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.TALLGRASS), new ItemStack(Items.DYE, 1, 13), 1);
    }
}
