package com.trials.modsquad.block;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.machine.BasicCable;
import com.trials.modsquad.block.tile.*;
import com.trials.modsquad.block.machine.*;
import com.trials.modsquad.block.ore.OreCopper;
import com.trials.modsquad.block.ore.OreLead;
import com.trials.modsquad.block.ore.OreTin;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.trials.modsquad.Ref.BlockReference.*;
import static net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity;

public class ModBlocks {

    //Ores
    public static Block oreCopper, oreTin, oreLead;
    public static Block blockCopper, blockTin, blockLead;

    //Machines
    public static Block grinder, electricFurnace, furnaceGen, charger, capacitor, solarPanel/*, toaster*/;

    //Cables
    public static Block leadCable;

    public static void init() {
        //Ores
        oreCopper = new OreCopper(Ref.OreReference.COPPER.getUnlocalizedName(), Ref.OreReference.COPPER.getRegistryName(), 2);
        oreTin = new OreTin(Ref.OreReference.TIN.getUnlocalizedName(), Ref.OreReference.TIN.getRegistryName(), 2);
        oreLead = new OreLead(Ref.OreReference.LEAD.getUnlocalizedName(), Ref.OreReference.LEAD.getRegistryName(), 2);

        blockCopper = new ModIngotBlock(Ref.OreBlockReference.COPPER.getUnlocalizedName(), Ref.OreBlockReference.COPPER.getRegistryName());
        blockTin = new ModIngotBlock(Ref.OreBlockReference.TIN.getUnlocalizedName(), Ref.OreBlockReference.TIN.getRegistryName());
        blockLead = new ModIngotBlock(Ref.OreBlockReference.LEAD.getUnlocalizedName(), Ref.OreBlockReference.LEAD.getRegistryName());

        //Machines
        grinder = new BlockGrinder(MACHINE_GRINDER.getUnlocalizedName(), MACHINE_GRINDER.getRegistryName());
        electricFurnace = new BlockElectricFurnace(MACHINE_FURNACE.getUnlocalizedName(), MACHINE_FURNACE.getRegistryName());
        furnaceGen = new BlockFurnaceGenerator(GENERATOR_FURNACE.getUnlocalizedName(), GENERATOR_FURNACE.getRegistryName());
        charger = new BlockCharger(MACHINE_CHARGER.getUnlocalizedName(), MACHINE_CHARGER.getRegistryName());
        capacitor = new BlockCapacitor(MACHINE_CAPACITOR.getUnlocalizedName(), MACHINE_CAPACITOR.getRegistryName());
        solarPanel = new BlockSolarPanel(GENERATOR_SOLAR.getUnlocalizedName(), GENERATOR_SOLAR.getRegistryName());
        //toaster = new BlockToaster(TOASTER.getUnlocalizedName(), TOASTER.getRegistryName());

        //Cables
        leadCable = new BasicCable(LEAD_CABLE.getUnlocalizedName(), LEAD_CABLE.getRegistryName());
    }

    public static void register() {
        // ---- Blocks ----
        //Ores
        registerBlock(oreCopper);
        registerBlock(oreTin);
        registerBlock(oreLead);

        registerBlock(blockCopper);
        registerBlock(blockTin);
        registerBlock(blockLead);

        //Machines
        registerBlock(grinder);
        registerBlock(electricFurnace);
        registerBlock(furnaceGen);
        registerBlock(charger);
        registerBlock(capacitor);
        registerBlock(solarPanel);
        //registerBlock(toaster);

        //Cables
        registerBlock(leadCable);


        // ---- tile ----
        //Machines
        registerTileEntity(TileGrinder.class, "Grinder");
        registerTileEntity(TileElectricFurnace.class, "Electric Furnace");
        registerTileEntity(TileFurnaceGenerator.class, "Furnace Generator");
        registerTileEntity(TileCharger.class, "Charger");
        registerTileEntity(TileCapacitor.class, "Capacitor");
        registerTileEntity(TileSolarPanel.class, "Solar Panel");
        registerTileEntity(TileToaster.class, "Toaster");

        //Cables
        registerTileEntity(TileCable.class, "Cable");
    }

    private static void registerBlock(Block block) {
        GameRegistry.register(block);
        Item item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        GameRegistry.register(item);
    }

    public static void registerRenders() {
        //Ores
        registerRender(oreCopper);
        registerRender(oreTin);
        registerRender(oreLead);

        registerRender(blockCopper);
        registerRender(blockTin);
        registerRender(blockLead);

        //Machines
        registerRender(grinder);
        registerRender(electricFurnace);
        registerRender(furnaceGen);
        registerRender(charger);
        registerRender(capacitor);
        registerRender(solarPanel);
        //registerRender(toaster);

        //Cables
        registerRender(leadCable);
    }

    private static void registerRender(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
