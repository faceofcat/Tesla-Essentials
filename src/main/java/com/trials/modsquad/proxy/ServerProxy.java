package com.trials.modsquad.proxy;

import com.trials.modsquad.common.blocks.ModBlocks;

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
