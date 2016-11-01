package com.trials.modsquad;

import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.gui.GUIHandler;
import com.trials.modsquad.item.ModItems;
import com.trials.modsquad.proxy.CommonProxy;
import com.trials.modsquad.recipe.TeslaRegistry;
import com.trials.modsquad.world.ModWorldGen;
import com.trials.net.ChatSync;
import com.trials.net.TileDataSync;
import com.trials.net.TileDataSync.Handler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModSquad.MODID, version = ModSquad.VERSION)
public class ModSquad
{
    public static final String MODID = "modsquad";
    public static final String VERSION = "1.0";
    public static SimpleNetworkWrapper channel;
    public static boolean allowCopperGen = false;
    public static boolean allowTinGen = false;
    public static boolean allowLeadGen = false;
    public static boolean electricPotatoBreakChance = false;
    public static int basePotatoBreakChance = 5;

    @Mod.Instance(value = MODID)
    public static ModSquad instance;

    @SidedProxy(clientSide = "com.trials.modsquad.proxy.ClientProxy", serverSide = "com.trials.modsquad.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static Logger logger = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ModSquad.logger = event.getModLog();
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        allowCopperGen = config.getBoolean("allowCopperGen", Configuration.CATEGORY_GENERAL, true, "Generates Copper Ore");
        allowTinGen = config.getBoolean("allowTinGen", Configuration.CATEGORY_GENERAL, true, "Generates Tin Ore");
        allowLeadGen = config.getBoolean("allowLeadGen", Configuration.CATEGORY_GENERAL, true, "Generates Lead Ore");
        electricPotatoBreakChance = config.getBoolean("electricPotatoBreakChance", Configuration.CATEGORY_GENERAL, true, "Allow's the electric potato to break");
        basePotatoBreakChance = config.getInt("potatoBreakChance", Configuration.CATEGORY_GENERAL, 5, 0, 50, "The electric potato base break chance");

        config.save();

        //Network communication
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

        //noinspection unchecked
        channel.registerMessage((Class) Handler.class, TileDataSync.class, 0, Side.CLIENT);

        ChatSync.createChatSyncForMod(MODID, channel); // Register chat handler

        // Item init and registration
        ModBlocks.init();
        ModBlocks.register();

        ModItems.init(electricPotatoBreakChance, basePotatoBreakChance);
        ModItems.register();

        proxy.preInit();
        GameRegistry.registerWorldGenerator(new ModWorldGen(allowCopperGen, allowTinGen, allowLeadGen), 0);

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

        OreDictionary.registerOre("blockCopper", ModBlocks.blockCopper);
        OreDictionary.registerOre("blockTin", ModBlocks.blockTin);
        OreDictionary.registerOre("blockLead", ModBlocks.blockLead);

        //Dusts
        OreDictionary.registerOre("dustCopper", ModItems.dustCopper);
        OreDictionary.registerOre("dustTin", ModItems.dustTin);
        OreDictionary.registerOre("dustLead", ModItems.dustLead);
        OreDictionary.registerOre("dustIron", ModItems.dustIron);
        OreDictionary.registerOre("dustGold", ModItems.dustGold);

        Ref.getDownloads();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Recipes
        proxy.init();
        TeslaRegistry.traditionalCraftingRegister();
        TeslaRegistry.registerGrinderCrafting();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        // Inter-mod interaction
        TeslaRegistry.registerOreDictCrafting();
        proxy.postInit();
    }
}
