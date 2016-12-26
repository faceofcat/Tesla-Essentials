package com.trials.modsquad.block;

import net.minecraft.util.IStringSerializable;

public class States {
    public enum ActiveState implements IStringSerializable {
        ACTIVE(true, "active"), INACTIVE(false, "inactive");

        private boolean state;
        private String name;

        ActiveState(boolean state, String name) {
            this.state = state;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
        public boolean getState() { return state; }

        @Override
        public String toString() {
            return name;
        }
    }
}
