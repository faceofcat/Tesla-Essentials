package com.trials.modsquad.common.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static void init() {

    }

    public static void register() {

    }

    public static void registerBlock(Block block) {
        GameRegistry.register(block);
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        GameRegistry.register(item);
    }

    public static void registerRenders() {

    }

    public static void registerRender(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0 , new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

}
