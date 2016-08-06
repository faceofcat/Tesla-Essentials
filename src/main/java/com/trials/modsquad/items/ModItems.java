package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.trials.modsquad.items.ModArmor.electricArmor;

public class ModItems {

    //Ingots
    public static Item ingotCopper, ingotTin, ingotLead;

    //Dusts
    public static Item dustCopper, dustTin, dustLead, dustIron, dustGold;

    public static Item test;

    //Armor
    public static Item electricHelmet, electricChestplate, electricLeggings, electricBoots, jetChestplate;

    //Tools
    public static Item terraSmasher;

    public static void init() {
        //Ingots
        ingotCopper = new ModIngot(Ref.IngotReference.COPPER.getUnlocalizedName(), Ref.IngotReference.COPPER.getRegistryName());
        ingotTin = new ModIngot(Ref.IngotReference.TIN.getUnlocalizedName(), Ref.IngotReference.TIN.getRegistryName());
        ingotLead = new ModIngot(Ref.IngotReference.LEAD.getUnlocalizedName(), Ref.IngotReference.LEAD.getRegistryName());

        //Dusts
        dustCopper = new ModDust(Ref.DustReference.COPPER.getUnlocalizedName(), Ref.DustReference.COPPER.getRegistryName());
        dustTin = new ModDust(Ref.DustReference.TIN.getUnlocalizedName(), Ref.DustReference.TIN.getRegistryName());
        dustLead = new ModDust(Ref.DustReference.LEAD.getUnlocalizedName(), Ref.DustReference.LEAD.getRegistryName());
        dustIron = new ModDust(Ref.DustReference.IRON.getUnlocalizedName(), Ref.DustReference.IRON.getRegistryName());
        dustGold = new ModDust(Ref.DustReference.GOLD.getUnlocalizedName(), Ref.DustReference.IRON.getRegistryName());

        test = new ItemTest();

        //Armor
        electricHelmet = new ModArmor(Ref.ItemReference.ELECTRIC_HELMET.getUnlocalizedName(), Ref.ItemReference.ELECTRIC_HELMET.getRegistryName(), electricArmor, 1, EntityEquipmentSlot.HEAD);
        electricChestplate = new ModArmor(Ref.ItemReference.ELECTRIC_CHESTPLATE.getUnlocalizedName(), Ref.ItemReference.ELECTRIC_CHESTPLATE.getRegistryName(), electricArmor, 1, EntityEquipmentSlot.CHEST);
        electricLeggings = new ModArmor(Ref.ItemReference.ELECTRIC_LEGGINGS.getUnlocalizedName(), Ref.ItemReference.ELECTRIC_LEGGINGS.getRegistryName(), electricArmor, 2, EntityEquipmentSlot.LEGS);
        electricBoots = new ModArmor(Ref.ItemReference.ELECTRIC_BOOTS.getUnlocalizedName(), Ref.ItemReference.ELECTRIC_BOOTS.getRegistryName(), electricArmor, 1, EntityEquipmentSlot.FEET);

        jetChestplate = new ModArmor(Ref.ItemReference.JET_CHESTPLATE.getUnlocalizedName(), Ref.ItemReference.JET_CHESTPLATE.getRegistryName(), electricArmor, 1, EntityEquipmentSlot.CHEST);

        //Tools
        terraSmasher = new TerraSmasher(Ref.ItemReference.TERRA_SMASHER.getUnlocalizedName(), Ref.ItemReference.TERRA_SMASHER.getRegistryName());

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
        GameRegistry.register(dustIron);
        GameRegistry.register(dustGold);

        GameRegistry.register(test);

        //Armor
        GameRegistry.register(electricHelmet);
        GameRegistry.register(electricChestplate);
        GameRegistry.register(electricLeggings);
        GameRegistry.register(electricBoots);

        GameRegistry.register(jetChestplate);

        //Tools
        GameRegistry.register(terraSmasher);

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
        registerRender(dustIron);
        registerRender(dustGold);

        registerRender(test);

        //Armor
        registerRender(electricHelmet);
        registerRender(electricChestplate);
        registerRender(electricLeggings);
        registerRender(electricBoots);

        registerRender(jetChestplate);

        //Tools
        registerRender(terraSmasher);

    }

    private static void registerRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

}
