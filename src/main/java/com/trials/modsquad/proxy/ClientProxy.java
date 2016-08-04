package com.trials.modsquad.proxy;

import com.trials.modsquad.block.ModBlocks;

public class ClientProxy implements CommonProxy {

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
