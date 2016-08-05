package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    //Ingots
    public static Item ingotCopper;
    public static Item ingotTin;
    public static Item ingotLead;

    //Dusts
    public static Item dustCopper;
    public static Item dustTin;
    public static Item dustLead;

    public static Item test;

    public static void init() {
        //Ingots
        ingotCopper = new ModIngot(Ref.IngotReference.COPPER.getUnlocalizedName(), Ref.IngotReference.COPPER.getRegistryName());
        ingotTin = new ModIngot(Ref.IngotReference.TIN.getUnlocalizedName(), Ref.IngotReference.TIN.getRegistryName());
        ingotLead = new ModIngot(Ref.IngotReference.LEAD.getUnlocalizedName(), Ref.IngotReference.LEAD.getRegistryName());

        //Dusts
        dustCopper = new ModDust(Ref.DustReference.COPPER.getUnlocalizedName(), Ref.DustReference.COPPER.getRegistryName());
        dustTin = new ModDust(Ref.DustReference.TIN.getUnlocalizedName(), Ref.DustReference.TIN.getRegistryName());
        dustLead = new ModDust(Ref.DustReference.LEAD.getUnlocalizedName(), Ref.DustReference.LEAD.getRegistryName());

        test = new ItemTest();

    }

    public static void register() {
        //Ingots
        GameRegistry.register(ingotCopper);
        GameRegistry.register(ingotTin);
        GameRegistry.register(ingotLead);

        //Dusts
        GameRegistry.register(dustCopper);
        GameRegistry.register(dustTin);
        GameRegistry.register(dustLead);

        GameRegistry.register(test);

    }

    public static void registerRenders() {
        //Ingots
        registerRender(ingotCopper);
        registerRender(ingotTin);
        registerRender(ingotLead);

        //Dusts
        registerRender(dustCopper);
        registerRender(dustTin);
        registerRender(dustLead);

        registerRender(test);

    }

    private static void registerRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

}
