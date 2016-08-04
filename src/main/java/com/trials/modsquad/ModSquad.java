package com.trials.modsquad;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ModSquad.MODID, version = ModSquad.VERSION)
public class ModSquad
{
    public static final String MODID = "modsquad";
    public static final String VERSION = "1.0";

    @Mod.Instance(value = MODID)
    public static ModSquad instance;

    @SidedProxy(clientSide = "com.trials.modsquad.proxy.ClientSide", serverSide = "com.trials.modsquad.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLInitializationEvent e){
        // Item init and registration
       // ModBlocks.init();
       // ModBlocks.register();

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Recipes
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLInitializationEvent e){
        // Inter-mod interaction
        proxy.postInit();
    }
}
