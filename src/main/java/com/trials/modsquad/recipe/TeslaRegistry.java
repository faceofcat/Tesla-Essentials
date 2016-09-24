package com.trials.modsquad.recipe;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TeslaRegistry{
    public static TeslaCraftingHandler teslaRegistry = new TeslaCraftingHandler();

    public static void traditionalCraftingRegister(){
        //Smelting ore
        GameRegistry.addSmelting(ModBlocks.oreCopper, new ItemStack(ModItems.ingotCopper), 0F);
        GameRegistry.addSmelting(ModBlocks.oreLead, new ItemStack(ModItems.ingotLead), 0F);
        GameRegistry.addSmelting(ModBlocks.oreTin, new ItemStack(ModItems.ingotTin), 0F);
        GameRegistry.addSmelting(ModItems.dustCopper, new ItemStack(ModItems.ingotCopper), 0F);
        GameRegistry.addSmelting(ModItems.dustLead, new ItemStack(ModItems.ingotLead), 0F);
        GameRegistry.addSmelting(ModItems.dustTin, new ItemStack(ModItems.ingotTin), 0F);
        GameRegistry.addSmelting(ModItems.dustIron, new ItemStack(Items.IRON_INGOT), 0F);
        GameRegistry.addSmelting(ModItems.dustGold, new ItemStack(Items.GOLD_INGOT), 0F);
        GameRegistry.addSmelting(ModItems.dustElectricAlloy, new ItemStack(ModItems.ingotElectricAlloy), 0F);
        //Materials
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.dustElectricAlloy, 2), new ItemStack(ModItems.dustCopper), new ItemStack(ModItems.dustCopper),
                new ItemStack(ModItems.dustTin), new ItemStack(Items.REDSTONE));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ingotLead, 9), new ItemStack(ModBlocks.blockLead));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ingotCopper, 9), new ItemStack(ModBlocks.blockCopper));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ingotTin, 9), new ItemStack(ModBlocks.blockTin));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.solarPanel),
                "C C",
                "TLT",
                "TIT",
                'C', ModItems.ingotCopper,
                'T', ModItems.ingotTin,
                'L', ModItems.dustLead,
                'I', Items.IRON_INGOT);
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
        //Vanilla Ores
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(ModItems.dustIron), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.GOLD_ORE), new ItemStack(ModItems.dustGold), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.COAL_ORE), new ItemStack(Items.COAL), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.DIAMOND_ORE), new ItemStack(Items.DIAMOND), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(Items.EMERALD), 2);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE), 12);
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Items.DYE, 1, 4), 9);
        //Misc
        for(int i = 0; i<16; ++i) {
            teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.WOOL, 1, i), new ItemStack(Items.STRING), 3);
            teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.CARPET, 1, i), new ItemStack(Items.STRING), 2);
        }
        teslaRegistry.registerGrinderRecipe(new ItemStack(Blocks.TALLGRASS, 1, 1), new ItemStack(Items.DYE, 1, 10), 1);
    }

    public static void registerOreDictCrafting()
    {
        //Mod Ores
        teslaRegistry.registerGrinderRecipe("oreLead", "dustLead", 2);
        teslaRegistry.registerGrinderRecipe("oreTin", "dustTin", 2);
        teslaRegistry.registerGrinderRecipe("oreCopper", "dustCopper", 2);
    }
}
