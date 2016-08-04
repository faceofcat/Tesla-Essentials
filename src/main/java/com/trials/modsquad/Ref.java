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
        LEAD_CABLE("lcable","BlockLeadCable"),
        MACHINE_GRINDER("grinder", "BlockGrinder");

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
        LEAD("oreLead", "BlockOreLead");        ;

        private String unlocalizedName, registryName;
        OreReference (String unlocalizedName, String registryName) {
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName() { return unlocalizedName; }

        public String getRegistryName() { return registryName; }
    }
}
