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
        MACHINEFURNACE("electric_furnace", "BlockElectricFurnace");

        private String unlocalizedName, registryName;
        BlockReference(String unlocalizedName, String registryName) {
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName() { return unlocalizedName; }

        public String getRegistryName() { return registryName; }
    }
}
