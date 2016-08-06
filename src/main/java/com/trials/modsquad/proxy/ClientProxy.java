package com.trials.modsquad.proxy;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.items.ModItems;

public class ClientProxy implements CommonProxy {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        ModBlocks.registerRenders();
        ModItems.registerRenders();
    }

    @Override
    public void postInit() {

    }
}
