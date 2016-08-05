package com.trials.modsquad;

import com.trials.modsquad.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Ref {

    public static final int GUI_ID_GRINDER = 0;
    public static final int GUI_ID_FURNACE_GEN = 1;
    public static final int GUI_ID_FURNACE = 2;
    public static final int GUI_ID_CHARGER = 3;


    public enum ItemReference{
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
