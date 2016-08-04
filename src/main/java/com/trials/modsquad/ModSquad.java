package com.trials.modsquad;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ModSquad.MODID, version = ModSquad.VERSION)
public class ModSquad
{
    public static final String MODID = "modsquad";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preInit(FMLInitializationEvent e){
        // Item init and registration
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Recipes
    }

    @EventHandler
    public void postInit(FMLInitializationEvent e){
        // Inter-mod interaction
    }
}
