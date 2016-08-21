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
        // Just for convenience
        public static ActiveState fromState(boolean state){ return ActiveState.values()[state?0:1]; }
        public boolean getState() { return state; }

        @Override
        public String toString() {
            return name;
        }
    }
}
