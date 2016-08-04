package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.tileentity.TileEntity;


/**
 * Created by Tjeltigre on 8/4/2016.
 */
public class TileGeneratorBase extends TileEntity implements ITeslaHolder,ITeslaProducer {

    private long storedPower;
    private long capacity;
    private long tickRate;
    private long production;
    private long temp;
    TileGeneratorBase(long storedPower,long capacity,long tickRate, long production)
    {

        this.storedPower = storedPower;
        this.capacity = capacity;
        this.tickRate = tickRate;
        this.production = production;
    }

    @Override
    public long getStoredPower() {
        return storedPower;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long takePower(long power, boolean simulated) {
        temp = 0;
        if(power>tickRate) {
            if (tickRate < storedPower) {
                storedPower = storedPower-tickRate;
                return tickRate;
            }
            temp = storedPower;
            storedPower = storedPower-storedPower;
            return temp;
        }
        if (power < storedPower) {
            storedPower = storedPower-power;
            return power;
        }
        temp = storedPower;
        storedPower = storedPower-storedPower;
        return temp;
    }

    public void update(boolean condition) {
        if(!this.worldObj.isRemote) {
            if(condition) {
                if(storedPower < capacity){
                    if((storedPower-production) < capacity)
                        storedPower=+production;
                    if((storedPower-production) > capacity)
                        storedPower = capacity;
                }
            }
        }

    }
}
