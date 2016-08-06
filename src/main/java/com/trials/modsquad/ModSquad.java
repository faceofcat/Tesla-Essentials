package com.trials.modsquad;

import com.trials.modsquad.Recipies.TeslaRegistry;
import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.gui.GUIHandler;
import com.trials.modsquad.items.ModItems;
import com.trials.modsquad.proxy.CommonProxy;
import com.trials.modsquad.world.ModWorldGen;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

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
    public void preInit(FMLPreInitializationEvent e){
        // Item init and registration
        ModBlocks.init();
        ModBlocks.register();

        ModItems.init();
        ModItems.register();

        GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);

        proxy.preInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());

        //OreDictionary stuff
        //Ores
        OreDictionary.registerOre("oreCopper", ModBlocks.oreCopper);
        OreDictionary.registerOre("oreTin", ModBlocks.oreTin);
        OreDictionary.registerOre("oreLead", ModBlocks.oreLead);

        //Ingots
        OreDictionary.registerOre("ingotCopper", ModItems.ingotCopper);
        OreDictionary.registerOre("ingotTin", ModItems.ingotTin);
        OreDictionary.registerOre("ingotLead", ModItems.ingotLead);

        //Dusts
        OreDictionary.registerOre("dustCopper", ModItems.dustCopper);
        OreDictionary.registerOre("dustTin", ModItems.dustTin);
        OreDictionary.registerOre("dustLead", ModItems.dustLead);
        OreDictionary.registerOre("dustIron", ModItems.dustIron);
        OreDictionary.registerOre("dustGold", ModItems.dustGold);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Recipes
        proxy.init();
        TeslaRegistry.registerGrinderCrafting();
        TeslaRegistry.traditionalCraftingRegister();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e){
        // Inter-mod interaction
        proxy.postInit();
    }
}
