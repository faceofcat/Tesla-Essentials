package com.trials.modsquad.block;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.Network.BasicCable;
import com.trials.modsquad.block.TileEntities.*;
import com.trials.modsquad.block.machines.BlockCharger;
import com.trials.modsquad.block.machines.BlockElectricFurnace;
import com.trials.modsquad.block.machines.BlockFurnaceGenerator;
import com.trials.modsquad.block.machines.BlockGrinder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.trials.modsquad.Ref.BlockReference.*;
import static net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity;

public class ModBlocks {

    //Ores
    public static Block oreCopper, oreTin, oreLead;

    //Machines
    public static Block grinder, electricFurnace, furnaceGen, charger;

    //Cables
    public static Block leadCable;

    public static void init() {
        //Ores
        oreCopper = new ModOre(Ref.OreReference.COPPER.getUnlocalizedName(), Ref.OreReference.COPPER.getRegistryName(), 1);
        oreTin = new ModOre(Ref.OreReference.TIN.getUnlocalizedName(), Ref.OreReference.TIN.getRegistryName(), 1);
        oreLead = new ModOre(Ref.OreReference.LEAD.getUnlocalizedName(), Ref.OreReference.LEAD.getRegistryName(), 2);

        //Machines
        grinder = new BlockGrinder(MACHINE_GRINDER.getUnlocalizedName(), MACHINE_GRINDER.getRegistryName());
        electricFurnace = new BlockElectricFurnace(MACHINE_FURNACE.getUnlocalizedName(), MACHINE_FURNACE.getRegistryName());
        furnaceGen = new BlockFurnaceGenerator(GENERATOR_FURNACE.getUnlocalizedName(), GENERATOR_FURNACE.getRegistryName());
        charger = new BlockCharger(MACHINE_CHARGER.getUnlocalizedName(), MACHINE_CHARGER.getRegistryName());

        //Cables
        leadCable = new BasicCable(LEAD_CABLE.getUnlocalizedName(), LEAD_CABLE.getRegistryName());
    }

    public static void register() {
        // ---- Blocks ----
        //Ores
        registerBlock(oreCopper);
        registerBlock(oreTin);
        registerBlock(oreLead);

        //Machines
        registerBlock(grinder);
        registerBlock(electricFurnace);
        registerBlock(furnaceGen);
        registerBlock(charger);

        //Cables
        registerBlock(leadCable);


        // ---- TileEntities ----
        //Machines
        registerTileEntity(TileGrinder.class, "Grinder");
        registerTileEntity(TileElectricFurnace.class, "Electric Furnace");
        registerTileEntity(TileFurnaceGenerator.class, "Furnace Generator");
        registerTileEntity(TileCharger.class, "Charger");

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

        //Machines
        registerRender(grinder);
        registerRender(electricFurnace);
        registerRender(furnaceGen);
        registerRender(charger);

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
