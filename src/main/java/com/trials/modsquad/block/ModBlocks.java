package com.trials.modsquad.block;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.TileEntities.TileGrinder;
import com.trials.modsquad.block.machines.BlockElectricFurnace;
import com.trials.modsquad.block.machines.BlockFurnaceGenerator;
import com.trials.modsquad.block.machines.BlockGrinder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.trials.modsquad.Ref.BlockReference.*;

public class ModBlocks {

    //Ores
    public static Block oreCopper;
    public static Block oreTin;
    public static Block oreLead;

    //Machines
    public static Block grinder;
    public static Block electricFurnace;
    public static Block furnaceGen;

    public static void init() {
        //Ores
        oreCopper = new ModOre(Ref.OreReference.COPPER.getUnlocalizedName(), Ref.OreReference.COPPER.getRegistryName(), 1);
        oreTin = new ModOre(Ref.OreReference.TIN.getUnlocalizedName(), Ref.OreReference.TIN.getRegistryName(), 1);
        oreLead = new ModOre(Ref.OreReference.LEAD.getUnlocalizedName(), Ref.OreReference.LEAD.getRegistryName(), 2);

        grinder = new BlockGrinder(MACHINE_GRINDER.getUnlocalizedName(), MACHINE_GRINDER.getRegistryName());
        electricFurnace = new BlockElectricFurnace(MACHINE_FURNACE.getUnlocalizedName(), MACHINE_FURNACE.getRegistryName());
        furnaceGen = new BlockFurnaceGenerator(GENERATOR_FURNACE.getUnlocalizedName(), GENERATOR_FURNACE.getRegistryName());
    }

    public static void register() {
        //Ores
        registerBlock(oreCopper);
        registerBlock(oreTin);
        registerBlock(oreLead);

        //Machines
        registerBlock(grinder);
        registerBlock(electricFurnace);
        registerBlock(furnaceGen);
    }

    private static void registerBlock(Block block) {
        GameRegistry.register(block);
        ItemBlock item = new ItemBlock(block);
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
    }

    private static void registerRender(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

}
