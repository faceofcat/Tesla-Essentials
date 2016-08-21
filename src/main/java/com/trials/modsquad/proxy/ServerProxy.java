package com.trials.modsquad.proxy;

import com.trials.modsquad.ModSquad;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

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
