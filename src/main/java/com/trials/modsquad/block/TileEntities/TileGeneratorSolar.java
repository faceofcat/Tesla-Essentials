package com.trials.modsquad.block.TileEntities;

import net.minecraft.util.ITickable;

/**
 * Created by Tjeltigre on 8/4/2016.
 */
public class TileGeneratorSolar extends TileGeneratorBase implements ITickable {

    TileGeneratorSolar(long storedPower, long capacity, long tickRate, long production) {
        super(storedPower, capacity, tickRate, production);
    }

    @Override
    public void update() {
        if(!worldObj.isRemote)
            super.update(0 < worldObj.getWorldTime()%24000
                    && 13000 > worldObj.getWorldTime()%24000);
    }
}
