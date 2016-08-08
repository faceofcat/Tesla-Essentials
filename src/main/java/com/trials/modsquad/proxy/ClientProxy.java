package com.trials.modsquad.proxy;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.block.TileEntities.*;
import com.trials.modsquad.items.ModItems;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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
