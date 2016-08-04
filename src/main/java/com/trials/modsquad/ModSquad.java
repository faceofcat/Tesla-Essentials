package com.trials.modsquad;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.block.ModOre;
import com.trials.modsquad.gui.GUIHandler;
import com.trials.modsquad.proxy.CommonProxy;
import com.trials.modsquad.world.ModWorldGen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ModSquad.MODID, version = ModSquad.VERSION)
public class ModSquad
{
    public static final String MODID = "modsquad";
    public static final String VERSION = "1.0";

    @Mod.Instance(value = MODID)
    public static ModSquad instance;

    @SidedProxy(clientSide = "com.trials.modsquad.proxy.ClientProxy", serverSide = "com.trials.modsquad.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLInitializationEvent e){
        // Item init and registration
        ModBlocks.init();
        ModBlocks.register();

        GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);

        proxy.preInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Recipes
        proxy.init();

        ModOre.registerBlocks();
    }

    @EventHandler
    public void postInit(FMLInitializationEvent e){
        // Inter-mod interaction
        proxy.postInit();
    }
}
