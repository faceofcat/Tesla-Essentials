package com.trials.modsquad.proxy;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.block.tile.TileToaster;
import com.trials.modsquad.block.tile.render.RendererToaster;
import com.trials.modsquad.item.ModItems;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@SuppressWarnings("unused")
public class ClientProxy implements CommonProxy {

    @Override
    public void preInit() {
        // Register tile that want data
    }

    @Override
    public void init() {
        ModBlocks.registerRenders();
        ModItems.registerRenders();

        ClientRegistry.bindTileEntitySpecialRenderer(TileToaster.class, new RendererToaster());
    }

    @Override
    public void postInit() {

    }
}
