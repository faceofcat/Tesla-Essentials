package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * Created by Tjeltigre on 8/4/2016.
 */
public class TileGeneratorBase extends TileEntity implements ITeslaHolder,ITeslaProducer, ITickable {

    private long storedPower;
    private long capacity;
    private long tickRate;
    private long production;
    private long temp;
    private boolean isOn;
    TileGeneratorBase(long storedPower,long capacity,long tickRate, long production)
    {

        this.storedPower = storedPower;
        this.capacity = capacity;
        this.tickRate = tickRate;
        this.production = production;
        isOn = true;
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
                return tickrate;
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

    @Override
    public void update() {
        if(!this.worldObj.isRemote) {
            if(isOn) {
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
