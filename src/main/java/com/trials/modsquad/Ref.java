package com.trials.modsquad;

import com.trials.modsquad.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ref {

    public static final int GUI_ID_GRINDER = 0;
    public static final int GUI_ID_FURNACE_GEN = 1;
    public static final int GUI_ID_FURNACE = 2;
    public static final int GUI_ID_CHARGER = 3;
    public static final int ITEM_ID_POTATO = 4;
    public static final int GUI_CHAT_DRFIGHT = 52;

    public static final int GUI_CHAT_POWER = Integer.MAX_VALUE-1;

    public enum ItemReference{
        ELECTRIC_HELMET("electricHelmet", "ItemElectricHelmet"),
        ELECTRIC_CHESTPLATE("electricChestplate", "ItemElectricChestplate"),
        ELECTRIC_LEGGINGS("electricLeggings", "ItemElectricLeggings"),
        ELECTRIC_BOOTS("electricBoots", "ItemElectricBoots"),
        TERRA_SMASHER("terraSmasher", "ItemTerraSmasher"),
        POWERED_POTATO("poweredPotato", "ItemPoweredPotato"),
        JET_CHESTPLATE("jetChestplate", "ItemJetChestplate"),
        MOD_INFO("modInfo", "ItemModInfo")
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
        MACHINE_CHARGER("charger", "BlockCharger"),
        TOASTER("toaster", "BlockToaster")
        ;

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

    public enum BreadReference {
        BREAD_SLICE("slicedBread", "ItemSlicedBread"),
        TOAST_SLICE("toast", "ItemToast")
        ;

        private String unlocalizedName, registeryName;
        BreadReference(String unlocalizedName, String registeryName) {
            this.unlocalizedName = unlocalizedName;
            this.registeryName = registeryName;
        }

        public String getUnlocalizedName() { return unlocalizedName; }
        public String getRegisteryName() { return registeryName; }

    }

    public static String downloads = "";

    public static void getDownloads() {
        String content;
        URLConnection connection;
        try {
            connection = new URL("https://minecraft.curseforge.com/projects/tesla-essentials?gameCategorySlug=mc-mods&projectID=248607").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
            // the pattern we want to search for
            Pattern p = Pattern.compile("<div class=\"info-data\">(.+?)</div>", Pattern.MULTILINE);
            Matcher m = p.matcher(content);
            List<String> matches = new ArrayList<>();
            // print all the matches that we find
            while (m.find())
            {
                matches.add(m.group(1));
            }
            String downloads = matches.get(2);
            Ref.downloads = downloads;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final CreativeTabs tabModSquad = new CreativeTabs("modSquad") { @Override public Item getTabIconItem() { return ModItems.jetChestplate; } };

}
