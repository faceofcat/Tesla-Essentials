package com.trials.modsquad.world;

import com.trials.modsquad.block.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ModWorldGen implements IWorldGenerator {

    private WorldGenerator gen_oreCopper;
    private WorldGenerator gen_oreTitanium;
    private WorldGenerator gen_oreNickel;
    private WorldGenerator gen_oreLead;

    public ModWorldGen() {
        gen_oreCopper = new WorldGenMinable(ModBlocks.oreCopper.getDefaultState(), 0);
    }

    private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chances, int minHeight, int maxHeight) {
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGen");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chances; i++) {
            int x = chunk_X * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x,y,z));
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case 0: //Overworld
                runGenerator(gen_oreCopper, world, random, chunkX, chunkZ, 40, 0, 64);
                break;
            case -1: //Nether

                break;
            case 1: //End

                break;
        }
    }
}
