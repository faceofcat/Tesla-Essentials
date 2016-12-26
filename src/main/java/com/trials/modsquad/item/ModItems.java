package com.trials.modsquad.item;

import com.trials.modsquad.Ref;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemArmor.ArmorMaterial electricArmor = EnumHelper.addArmorMaterial("electricalArmor", "modsquad:electricalArmor", 15, new int[]{3,8,6,3}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 4F);

    //Ingots
    public static Item ingotCopper, ingotTin, ingotLead, ingotElectricAlloy;

    //Dusts
    public static Item dustCopper, dustTin, dustLead, dustIron, dustGold, dustElectricAlloy;

    //Armor
    public static Item electricHelmet, electricChestplate, electricLeggings, electricBoots, jetChestplate;

    //Tools
    public static Item terraSmasher, poweredPotato;

    //Food
    public static Item breadSlice, toastSlice;

    //Misc
    public static Item gameInfo;

    public static void init(boolean electricPotatoBreakChance, int basePotatoBreakChance) {
        //Ingots
        ingotCopper = new ModIngot(Ref.IngotReference.COPPER.getUnlocalizedName(), Ref.IngotReference.COPPER.getRegistryName());
        ingotTin = new ModIngot(Ref.IngotReference.TIN.getUnlocalizedName(), Ref.IngotReference.TIN.getRegistryName());
        ingotLead = new ModIngot(Ref.IngotReference.LEAD.getUnlocalizedName(), Ref.IngotReference.LEAD.getRegistryName());
        ingotElectricAlloy = new ModIngot(Ref.IngotReference.ELECTRICALLOY.getUnlocalizedName(), Ref.IngotReference.ELECTRICALLOY.getRegistryName());

        //Dusts
        dustCopper = new ModDust(Ref.DustReference.COPPER.getUnlocalizedName(), Ref.DustReference.COPPER.getRegistryName());
        dustTin = new ModDust(Ref.DustReference.TIN.getUnlocalizedName(), Ref.DustReference.TIN.getRegistryName());
        dustLead = new ModDust(Ref.DustReference.LEAD.getUnlocalizedName(), Ref.DustReference.LEAD.getRegistryName());
        dustIron = new ModDust(Ref.DustReference.IRON.getUnlocalizedName(), Ref.DustReference.IRON.getRegistryName());
        dustGold = new ModDust(Ref.DustReference.GOLD.getUnlocalizedName(), Ref.DustReference.GOLD.getRegistryName());
        dustElectricAlloy = new ModDust(Ref.DustReference.ELECTRICALLOY.getUnlocalizedName(), Ref.DustReference.ELECTRICALLOY.getRegistryName());

        //Armor
        electricHelmet = new ModArmor(Ref.ItemReference.ELECTRIC_HELMET.getUnlocalizedName(), Ref.ItemReference.ELECTRIC_HELMET.getRegistryName(), electricArmor, 1, EntityEquipmentSlot.HEAD);
        electricChestplate = new ModArmor(Ref.ItemReference.ELECTRIC_CHESTPLATE.getUnlocalizedName(), Ref.ItemReference.ELECTRIC_CHESTPLATE.getRegistryName(), electricArmor, 1, EntityEquipmentSlot.CHEST);
        electricLeggings = new ModArmor(Ref.ItemReference.ELECTRIC_LEGGINGS.getUnlocalizedName(), Ref.ItemReference.ELECTRIC_LEGGINGS.getRegistryName(), electricArmor, 2, EntityEquipmentSlot.LEGS);
        electricBoots = new ModArmor(Ref.ItemReference.ELECTRIC_BOOTS.getUnlocalizedName(), Ref.ItemReference.ELECTRIC_BOOTS.getRegistryName(), electricArmor, 1, EntityEquipmentSlot.FEET);

        jetChestplate = new ModArmor(Ref.ItemReference.JET_CHESTPLATE.getUnlocalizedName(), Ref.ItemReference.JET_CHESTPLATE.getRegistryName(), electricArmor, 1, EntityEquipmentSlot.CHEST);

        //Tools
        terraSmasher = new TerraSmasher(Ref.ItemReference.TERRA_SMASHER.getUnlocalizedName(), Ref.ItemReference.TERRA_SMASHER.getRegistryName());
        poweredPotato = new PoweredPotato(Ref.ItemReference.POWERED_POTATO.getUnlocalizedName(), Ref.ItemReference.POWERED_POTATO.getRegistryName(), electricPotatoBreakChance, basePotatoBreakChance);

        //Food
        breadSlice = new ItemBread(Ref.BreadReference.BREAD_SLICE.getUnlocalizedName(), Ref.BreadReference.BREAD_SLICE.getRegisteryName());
        toastSlice = new ItemBread(Ref.BreadReference.TOAST_SLICE.getUnlocalizedName(), Ref.BreadReference.TOAST_SLICE.getRegisteryName());

        //Misc
        gameInfo = new ItemGameInfo(Ref.ItemReference.MOD_INFO.getUnlocalizedName(), Ref.ItemReference.MOD_INFO.getRegistryName());

    }

    public static void register() {
        //Ingots
        GameRegistry.register(ingotCopper);
        GameRegistry.register(ingotTin);
        GameRegistry.register(ingotLead);
        GameRegistry.register(ingotElectricAlloy);

        //Dusts
        GameRegistry.register(dustCopper);
        GameRegistry.register(dustTin);
        GameRegistry.register(dustLead);
        GameRegistry.register(dustIron);
        GameRegistry.register(dustGold);
        GameRegistry.register(dustElectricAlloy);

        //Armor
        GameRegistry.register(electricHelmet);
        GameRegistry.register(electricChestplate);
        GameRegistry.register(electricLeggings);
        GameRegistry.register(electricBoots);

        GameRegistry.register(jetChestplate);

        //Tools
        GameRegistry.register(terraSmasher);
        GameRegistry.register(poweredPotato);

        //Food
        GameRegistry.register(breadSlice);
        GameRegistry.register(toastSlice);

        //Misc

        GameRegistry.register(gameInfo);

    }

    public static void registerRenders() {
        //Ingots
        registerRender(ingotCopper);
        registerRender(ingotTin);
        registerRender(ingotLead);
        registerRender(ingotElectricAlloy);

        //Dusts
        registerRender(dustCopper);
        registerRender(dustTin);
        registerRender(dustLead);
        registerRender(dustIron);
        registerRender(dustGold);
        registerRender(dustElectricAlloy);

        //Armor
        registerRender(electricHelmet);
        registerRender(electricChestplate);
        registerRender(electricLeggings);
        registerRender(electricBoots);

        registerRender(jetChestplate);

        //Tools
        registerRender(terraSmasher);
        registerRender(poweredPotato);

        //Food
        registerRender(breadSlice);
        registerRender(toastSlice);

        //Misc
        registerRender(gameInfo);

    }

    private static void registerRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

}
