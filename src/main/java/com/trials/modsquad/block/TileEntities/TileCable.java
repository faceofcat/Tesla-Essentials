package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.block.ModBlocks;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Tjeltigre on 8/5/2016.
 */
public class TileCable extends TileEntity implements ITeslaHolder, ITeslaConsumer, ITickable {
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
    public long getStoredPower () {

        return this.stored;
    }

    @Override
    public long givePower (long Tesla, boolean simulated) {

        final long acceptedTesla = Math.min(this.getCapacity() - this.stored, Math.min(this.getInputRate(), Tesla));

        if (!simulated)
            this.stored += acceptedTesla;

        return acceptedTesla;
    }

    @Override
    public long getCapacity () {

        return this.capacity;
    }
    /**
     * Sets the capacity of the the container. If the existing stored power is more than the
     * new capacity, the stored power will be decreased to match the new capacity.
     *
     * @param capacity The new capacity for the container.
     * @return The instance of the container being updated.
     */
    public TileCable setCapacity (long capacity) {

        this.capacity = capacity;

        if (this.stored > capacity)
            this.stored = capacity;

        return this;
    }

    /**
     * Gets the maximum amount of Tesla power that can be accepted by the container.
     *
     * @return The amount of Tesla power that can be accepted at any time.
     */
    public long getInputRate () {

        return this.inputRate;
    }

    /**
     * Sets the maximum amount of Tesla power that can be accepted by the container.
     *
     * @param rate The amount of Tesla power to accept at a time.
     * @return The instance of the container being updated.
     */
    public TileCable setInputRate (long rate) {

        this.inputRate = rate;
        return this;
    }

    /**
     * Gets the maximum amount of Tesla power that can be pulled from the container.
     *
     * @return The amount of Tesla power that can be extracted at any time.
     */
    public long getOutputRate () {

        return this.outputRate;
    }

    /**
     * Sets the maximum amount of Tesla power that can be pulled from the container.
     *
     * @param rate The amount of Tesla power that can be extracted.
     * @return The instance of the container being updated.
     */
    public TileCable setOutputRate (long rate) {

        this.outputRate = rate;
        return this;
    }

    /**
     * Sets both the input and output rates of the container at the same time. Both rates will
     * be the same.
     *
     * @param rate The input/output rate for the Tesla container.
     * @return The instance of the container being updated.
     */
    public TileCable setTransferRate (long rate) {

        this.setInputRate(rate);
        this.setOutputRate(rate);
        return this;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) { return capability == TeslaCapabilities.CAPABILITY_PRODUCER && capability != TeslaCapabilities.CAPABILITY_CONSUMER
            || capability == TeslaCapabilities.CAPABILITY_HOLDER && capability != TeslaCapabilities.CAPABILITY_CONSUMER; }

    @Override
    public void update() {
        if(!worldObj.isRemote){
            if(stored < capacity){
                x = this.getPos().getX();
                y = this.getPos().getY();
                z = this.getPos().getZ();
                TileEntity e;
                if((e=worldObj.getTileEntity(new BlockPos(x,y+1,z)))!=null && e.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, ModBlocks.getRelativeFace(pos, e.getPos()).getOpposite()))
                    capacity+=((ITeslaProducer) e).takePower(Math.min(inputRate, capacity-stored), false);
            }

        }
    }
}
