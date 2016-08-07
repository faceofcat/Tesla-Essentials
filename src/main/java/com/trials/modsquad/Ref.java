package com.trials.modsquad;

import com.trials.modsquad.items.ModItems;
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

    public enum ItemReference{
        ELECTRIC_HELMET("electricHelmet", "ItemElectricHelmet"),
        ELECTRIC_CHESTPLATE("electricChestplate", "ItemElectricChestplate"),
        ELECTRIC_LEGGINGS("electricLeggings", "ItemElectricLeggings"),
        ELECTRIC_BOOTS("electricBoots", "ItemElectricBoots"),
        TERRA_SMASHER("terraSmasher", "ItemTerraSmasher"),
        POWERED_POTATO("poweredPotato", "ItemPoweredPotato"),
        JET_CHESTPLATE("jetChestplate", "ItemJetChestplate")
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
        GENERATOR_SOLAR("solarPanel", "SolarPanel"),
        MACHINE_CAPACITOR("capacitor", "BlockCapacitor"),
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

    public enum OreBlockReference {
        COPPER("blockCopper", "BlockCopper"),
        TIN("blockTin", "BlockTin"),
        LEAD("blockLead", "BlockLead");

        private String unlocalizedName, registryName;
        OreBlockReference (String unlocalizedName, String registryName) {
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName() { return unlocalizedName; }

        public String getRegistryName() { return registryName; }
    }

    public enum IngotReference{
        COPPER("ingotCopper", "ItemIngotCopper"),
        TIN("ingotTin", "ItemIngotTin"),
        LEAD("ingotLead", "ItemIngotLead"),
        ELECTRICALLOY("ingotElectricAlloy","ItemIngotElectricAlloy")
        ;
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
        LEAD("dustLead", "ItemDustLead"),
        IRON("dustIron", "ItemDustIron"),
        GOLD("dustGold", "ItemDustGold"),
        ELECTRICALLOY("dustElectricAlloy","ItemDustElectricAlloy")
        ;

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
