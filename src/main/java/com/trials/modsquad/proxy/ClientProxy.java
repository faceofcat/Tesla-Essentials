package com.trials.modsquad.proxy;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.item.ModItems;

public class ClientProxy implements CommonProxy {

    @Override
    public void preInit() {
        // Register TileEntities that want data
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
