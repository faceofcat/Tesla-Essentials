package com.trials.modsquad.block;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.Network.BasicCable;
import com.trials.modsquad.block.TileEntities.*;
import com.trials.modsquad.block.TileEntities.renderer.CableRenderer;
import com.trials.modsquad.block.machines.*;
import com.trials.modsquad.block.ores.OreCopper;
import com.trials.modsquad.block.ores.OreLead;
import com.trials.modsquad.block.ores.OreTin;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.trials.modsquad.Ref.BlockReference.*;
import static net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity;

public class ModBlocks {

    //Ores
    public static Block oreCopper, oreTin, oreLead;
    public static Block blockCopper, blockTin, blockLead;

    //Machines
    public static Block grinder, electricFurnace, furnaceGen, charger, capacitor;

    //Cables
    public static Block leadCable;

    public static void init() {
        //Ores
        oreCopper = new OreCopper(Ref.OreReference.COPPER.getUnlocalizedName(), Ref.OreReference.COPPER.getRegistryName());
        oreTin = new OreTin(Ref.OreReference.TIN.getUnlocalizedName(), Ref.OreReference.TIN.getRegistryName());
        oreLead = new OreLead(Ref.OreReference.LEAD.getUnlocalizedName(), Ref.OreReference.LEAD.getRegistryName());

        blockCopper = new ModIngotBlock(Ref.OreBlockReference.COPPER.getUnlocalizedName(), Ref.OreBlockReference.COPPER.getRegistryName());
        blockTin = new ModIngotBlock(Ref.OreBlockReference.TIN.getUnlocalizedName(), Ref.OreBlockReference.TIN.getRegistryName());
        blockLead = new ModIngotBlock(Ref.OreBlockReference.LEAD.getUnlocalizedName(), Ref.OreBlockReference.LEAD.getRegistryName());

        //Machines
        grinder = new BlockGrinder(MACHINE_GRINDER.getUnlocalizedName(), MACHINE_GRINDER.getRegistryName());
        electricFurnace = new BlockElectricFurnace(MACHINE_FURNACE.getUnlocalizedName(), MACHINE_FURNACE.getRegistryName());
        furnaceGen = new BlockFurnaceGenerator(GENERATOR_FURNACE.getUnlocalizedName(), GENERATOR_FURNACE.getRegistryName());
        charger = new BlockCharger(MACHINE_CHARGER.getUnlocalizedName(), MACHINE_CHARGER.getRegistryName());
        capacitor = new BlockCapacitor(MACHINE_CAPACITOR.getUnlocalizedName(), MACHINE_CAPACITOR.getRegistryName());

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

        //Cables
        registerBlock(leadCable);


        // ---- TileEntities ----
        //Machines
        registerTileEntity(TileGrinder.class, "Grinder");
        registerTileEntity(TileElectricFurnace.class, "Electric Furnace");
        registerTileEntity(TileFurnaceGenerator.class, "Furnace Generator");
        registerTileEntity(TileCharger.class, "Charger");
        registerTileEntity(TileCapacitor.class, "Capacitor");

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

        //Cables
        registerRender(leadCable);
    }

    private static void registerRender(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

    public static EnumFacing getRelativeFace(BlockPos origin, BlockPos pos){
        if(origin.getX()>pos.getX()) return EnumFacing.WEST;
        if(origin.getX()<pos.getX()) return EnumFacing.EAST;
        if(origin.getZ()>pos.getZ()) return EnumFacing.NORTH;
        if(origin.getZ()<pos.getZ()) return EnumFacing.SOUTH;
        if(origin.getY()>pos.getY()) return EnumFacing.DOWN;
        return EnumFacing.UP; // Default
    }

}
