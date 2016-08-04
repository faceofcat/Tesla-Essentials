package com.trials.modsquad.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModOres {

    public enum ORES {
        COPPER("Copper"), TIN("Tin"), SILVER("Silver"), OIL("Oil"), OSMIUM("Osmium"), TITANIUM("Titanium"), CHROMIUM("Chromium"), NICKEL("Nickel"), LEAD("Lead");

        private String name;
        ORES(String name) {
            this.name = name;
        }

        public String getName() { return name; }

    }

    public void init() {
        for (i = 1; i == ORES.values().length; i++) {
            
        }
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
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

}
