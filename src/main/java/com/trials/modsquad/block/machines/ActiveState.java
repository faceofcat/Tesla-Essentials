package com.trials.modsquad.block.machines;

import net.minecraft.util.IStringSerializable;

public enum ActiveState implements IStringSerializable{
    ACTIVE(0, "active"),
    INACTIVE(1, "inactive");

    private int ID;
    private String name;

    ActiveState(int ID, String name){
        this.ID = ID;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getID(){
        return ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
