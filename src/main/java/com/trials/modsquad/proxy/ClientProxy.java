package com.trials.modsquad.proxy;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.items.ModItems;

public class ClientProxy implements CommonProxy {

    @Override
    public void preInit() {
        ModBlocks.registerRenders();
        ModItems.registerRenders();
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }
}
