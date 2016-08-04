package com.trials.modsquad.proxy;

import com.trials.modsquad.common.blocks.machines.ModBlocks;

public class ServerProxy implements CommonProxy {

    @Override
    public void preInit() {
        ModBlocks.registerRenders();
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }
}
