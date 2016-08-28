package com.trials.modsquad.proxy;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@SuppressWarnings("unused")
public class ServerProxy implements CommonProxy {

    @Override
    public void preInit() {
        // Register tile that want data
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event){

    }
}
