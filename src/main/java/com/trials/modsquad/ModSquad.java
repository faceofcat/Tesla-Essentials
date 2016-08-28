package com.trials.modsquad;

import com.trials.modsquad.recipe.TeslaRegistry;
import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.gui.GUIHandler;
import com.trials.modsquad.item.ModItems;
import com.trials.modsquad.proxy.CommonProxy;
import com.trials.modsquad.proxy.TileDataSync;
import com.trials.modsquad.proxy.TileDataSync.Handler;
import com.trials.modsquad.world.ModWorldGen;
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

@Mod(modid = ModSquad.MODID, version = ModSquad.VERSION)
public class ModSquad
{
    public static final String MODID = "modsquad";
    public static final String VERSION = "1.0";
    public static SimpleNetworkWrapper channel;
    public static boolean allowCopperGen = false;
    public static boolean allowTinGen = false;
    public static boolean allowLeadGen = false;

    @Mod.Instance(value = MODID)
    public static ModSquad instance;

    @SidedProxy(clientSide = "com.trials.modsquad.proxy.ClientProxy", serverSide = "com.trials.modsquad.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        //Network communication
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

        //noinspection unchecked
        channel.registerMessage((Class) Handler.class, TileDataSync.class, 0, Side.CLIENT);

        // Item init and registration
        ModBlocks.init();
        ModBlocks.register();

        ModItems.init();
        ModItems.register();
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        allowCopperGen = config.getBoolean("allowCopperGen", Configuration.CATEGORY_GENERAL, true, "Generates Copper Ore");
        allowTinGen = config.getBoolean("allowTinGen", Configuration.CATEGORY_GENERAL, true, "Generates Tin Ore");
        allowLeadGen = config.getBoolean("allowLeadGen", Configuration.CATEGORY_GENERAL, true, "Generates Lead Ore");
        config.save();

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

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Recipes
        proxy.init();
        TeslaRegistry.traditionalCraftingRegister();
        TeslaRegistry.registerGrinderCrafting();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e){
        // Inter-mod interaction
        TeslaRegistry.registerOreDictCrafting();
        proxy.postInit();
    }
}
