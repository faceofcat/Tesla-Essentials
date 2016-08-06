package com.trials.modsquad;

import com.trials.modsquad.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class Ref {

    public static final int GUI_ID_GRINDER = 0;
    public static final int GUI_ID_FURNACE_GEN = 1;
    public static final int GUI_ID_FURNACE = 2;
    public static final int GUI_ID_CHARGER = 3;

    public static ItemArmor.ArmorMaterial electricArmor = EnumHelper.addArmorMaterial("electricalArmor", "modsquad:electricalArmor", 15, new int[]{3,8,6,3}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 4F);
    public static ItemArmor.ArmorMaterial deadArmor = EnumHelper.addArmorMaterial("deadArmor", "modsquad:deadArmor", 0, new int[]{0,0,0,0}, 0, null, 0F);

    public enum ItemReference{
        ELECTRIC_HELMET("electricHelmet", "ItemElectricHelmet"),
        ELECTRIC_CHESTPLATE("electricChestplate", "ItemElectricChestplate"),
        ELECTRIC_LEGGINGS("electricLeggings", "ItemElectricLeggings"),
        ELECTRIC_BOOTS("electricBoots", "ItemElectricBoots")
        ;

        private String unlocalizedName, registryName;
        ItemReference(String unlocalizedName, String registryName){
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName(){ return unlocalizedName; }
        public String getRegistryName(){ return registryName; }
    }

    public enum BlockReference {
        LEAD_CABLE("lcable","BlockLeadCable"),
        MACHINE_GRINDER("grinder", "BlockGrinder"),
        MACHINE_FURNACE("electricFurnace", "BlockElectricFurnace"),
        GENERATOR_FURNACE("generatorFurnace", "FurnaceGenerator"),
        GENERATOR_SOALR("solarPanel", "SolarPanel"),
        MACHINE_CHARGER("charger", "BlockCharger");

        private String unlocalizedName, registryName;
        BlockReference(String unlocalizedName, String registryName) {
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName() { return unlocalizedName; }

        public String getRegistryName() { return registryName; }
    }

    public enum OreReference {
        COPPER("oreCopper", "BlockOreCopper"),
        TIN("oreTin", "BlockOreTin"),
        LEAD("oreLead", "BlockOreLead");

        private String unlocalizedName, registryName;
        OreReference (String unlocalizedName, String registryName) {
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName() { return unlocalizedName; }

        public String getRegistryName() { return registryName; }
    }

    public enum IngotReference{
        COPPER("ingotCopper", "ItemIngotCopper"),
        TIN("ingotTin", "ItemIngotTin"),
        LEAD("ingotLead", "ItemIngotLead");

        private String unlocalizedName, registryName;
        IngotReference(String unlocalizedName, String registryName){
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName(){ return unlocalizedName; }
        public String getRegistryName(){ return registryName; }
    }

    public enum DustReference{
        COPPER("dustCopper", "ItemDustCopper"),
        TIN("dustTin", "ItemDustTin"),
        LEAD("dustLead", "ItemDustLead");

        private String unlocalizedName, registryName;
        DustReference(String unlocalizedName, String registryName){
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName(){ return unlocalizedName; }
        public String getRegistryName(){ return registryName; }
    }

    public static final CreativeTabs tabModSquad = new CreativeTabs("modSquad") {
        @Override public Item getTabIconItem() {
            return ModItems.ingotCopper;
        }
    };

}
