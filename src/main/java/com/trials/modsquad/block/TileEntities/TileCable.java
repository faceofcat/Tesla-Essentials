package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Tjeltigre on 8/5/2016.
 */
public class TileCable extends TileEntity implements ITickable {
    /**
     * The amount of stored Tesla power.
     */
    private long stored;

    /**
     * The maximum amount of Tesla power that can be stored.
     */
    private long capacity;

    /**
     * The maximum amount of Tesla power that can be accepted.
     */
    private long inputRate;

    /**
     * The maximum amount of Tesla power that can be extracted
     */
    private long outputRate;

    int x;
    int y;
    int z;

    public TileCable() {

        this(5000, 50, 50);
    }
    /**
     * Constructor for setting the basic values. Will not construct with any stored power.
     *
     * @param capacity The maximum amount of Tesla power that the container should hold.
     * @param input The maximum rate of power that can be accepted at a time.
     * @param output The maximum rate of power that can be extracted at a time.
     */
    public TileCable(long capacity, long input, long output) {

        this(0, capacity, input, output);
    }

    /**
     * Constructor for setting all of the base values, including the stored power.
     *
     * @param power The amount of stored power to initialize the container with.
     * @param capacity The maximum amount of Tesla power that the container should hold.
     * @param input The maximum rate of power that can be accepted at a time.
     * @param output The maximum rate of power that can be extracted at a time.
     */
    public TileCable(long power, long capacity, long input, long output) {
        this.stored = power;
        this.capacity = capacity;
        this.inputRate = input;
        this.outputRate = output;
    }
    @Override
    public void update() {
        if(!worldObj.isRemote){
            if(stored < capacity){
                x = this.getPos().getX();
                y = this.getPos().getY();
                z = this.getPos().getZ();
                TileEntity e;
                if((e=worldObj.getTileEntity(new BlockPos(x,y+1,z)))!=null && e.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, EnumFacing.UP))
                    capacity+=((ITeslaProducer) e).takePower(Math.min(inputRate, capacity-stored), false);
            }

        }
    }
}
