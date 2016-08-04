package com.trials.modsquad.world;

import com.trials.modsquad.block.ModBlocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ModWorldGen implements IWorldGenerator {

    private WorldGenerator gen_oreCopper;
    private WorldGenerator gen_oreTin;
    private WorldGenerator gen_oreSilver;
    private WorldGenerator gen_oreOil;
    private WorldGenerator gen_oreOsmium;
    private WorldGenerator gen_oreTitanium;
    private WorldGenerator gen_oreChromium;
    private WorldGenerator gen_oreNickel;
    private WorldGenerator gen_oreLead;

    public ModWorldGen() {
        this.gen_oreCopper = new WorldGenMinable(ModBlocks.oreCopper.getDefaultState(), 0);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case 0: //Overworld

                break;
            case -1: //Nether

                break;
            case 1: //End

                break;
        }
    }
}
