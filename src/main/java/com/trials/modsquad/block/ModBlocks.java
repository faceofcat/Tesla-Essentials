package com.trials.modsquad.block;

import com.trials.modsquad.Ref;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    //Ores
    public static Block oreCopper;
    public static Block oreTitanium;
    public static Block oreNickel;
    public static Block oreLead;

    public static void init() {
        //Ores
        oreCopper = new ModOre(Ref.OreReference.COPPER.getUnlocalizedName(), Ref.OreReference.COPPER.getRegistryName());
        oreTitanium = new ModOre(Ref.OreReference.TITANIUM.getUnlocalizedName(), Ref.OreReference.TITANIUM.getRegistryName());
        oreNickel = new ModOre(Ref.OreReference.NICKEL.getUnlocalizedName(), Ref.OreReference.NICKEL.getRegistryName());
        oreLead = new ModOre(Ref.OreReference.LEAD.getUnlocalizedName(), Ref.OreReference.LEAD.getRegistryName());

    }

    public static void register() {
        //Ores
        registerBlock(oreCopper);
        registerBlock(oreTitanium);
        registerBlock(oreNickel);
        registerBlock(oreLead);

    }

    public static void registerBlock(Block block) {
        GameRegistry.register(block);
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        GameRegistry.register(item);
    }

    public static void registerRenders() {
        registerRender(oreCopper);
        registerRender(oreTitanium);
        registerRender(oreNickel);
        registerRender(oreLead);
    }

    public static void registerRender(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

}
