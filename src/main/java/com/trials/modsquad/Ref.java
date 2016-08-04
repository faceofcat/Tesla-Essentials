package com.trials.modsquad;

public class Ref {


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
        LEAD_CABLE("lcable","BlockLeadCable");

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
        SILVER("oreSilver", "BlockOreSilver"),
        OIL("oreOil", "BlockOreOil"),
        OSMIUM("oreOsmium", "BlockOreOsmium"),
        TITANIUM("oreTitanium", "BlockOreTitanium"),
        CHROMIUM("oreChromium", "BlockOreChromium"),
        NICKEL("oreNickel", "BlockOreNickel"),
        LEAD("oreLead", "BlockOreLead"),
        ;

        private String unlocalizedName, registryName;
        OreReference (String unlocalizedName, String registryName) {
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName() { return unlocalizedName; }

        public String getRegistryName() { return registryName; }
    }
}
