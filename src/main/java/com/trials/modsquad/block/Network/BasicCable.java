package com.trials.modsquad.block.Network;

import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemClock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * Created by Tjeltigre on 8/4/2016.
 */
@SuppressWarnings("unused")
public class BasicCable extends Block implements ITeslaConsumer, ITeslaHolder, ITeslaProducer, INBTSerializable<NBTTagCompound> {
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

    private Ref.BlockReference type;

    public BasicCable() {

        this(5000, 50, 50, 1);
    }
    /**
     * Constructor for setting the basic values. Will not construct with any stored power.
     *
     * @param capacity The maximum amount of Tesla power that the container should hold.
     * @param input The maximum rate of power that can be accepted at a time.
     * @param output The maximum rate of power that can be extracted at a time.
     */
    public BasicCable(long capacity, long input, long output, int index) {

        this(0, capacity, input, output, index);
    }

    /**
     * Constructor for setting all of the base values, including the stored power.
     *
     * @param power The amount of stored power to initialize the container with.
     * @param capacity The maximum amount of Tesla power that the container should hold.
     * @param input The maximum rate of power that can be accepted at a time.
     * @param output The maximum rate of power that can be extracted at a time.
     */
    public BasicCable(long power, long capacity, long input, long output, int index) {
        super(Material.ANVIL);
        this.stored = power;
        this.capacity = capacity;
        this.inputRate = input;
        this.outputRate = output;
        for(Ref.BlockReference b : Ref.BlockReference.values())
            if(b.ordinal()==index){
                type = b;
                setUnlocalizedName(type.getUnlocalizedName());
                setRegistryName(type.getRegistryName());
                break;
            }
    }
    /**
     * Constructor for creating an instance directly from a compound tag. This expects that the
     * compound tag has some of the required data. @See {@link #deserializeNBT(NBTTagCompound)}
     * for precise info on what is expected. This constructor will only set the stored power if
     * it has been written on the compound tag.
     *
     * @param dataTag The NBTCompoundTag to read the important data from.
     */
    public BasicCable(NBTTagCompound dataTag) {
        super(Material.ANVIL);
        this.deserializeNBT(dataTag);
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
    public long takePower (long Tesla, boolean simulated) {

        final long removedPower = Math.min(this.stored, Math.min(this.getOutputRate(), Tesla));

        if (!simulated)
            this.stored -= removedPower;

        return removedPower;
    }

    @Override
    public long getCapacity () {

        return this.capacity;
    }

    @Override
    public NBTTagCompound serializeNBT () {

        final NBTTagCompound dataTag = new NBTTagCompound();
        dataTag.setLong("TeslaPower", this.stored);
        dataTag.setLong("TeslaCapacity", this.capacity);
        dataTag.setLong("TeslaInput", this.inputRate);
        dataTag.setLong("TeslaOutput", this.outputRate);

        return dataTag;
    }

    @Override
    public void deserializeNBT (NBTTagCompound nbt) {

        this.stored = nbt.getLong("TeslaPower");

        if (nbt.hasKey("TeslaCapacity"))
            this.capacity = nbt.getLong("TeslaCapacity");

        if (nbt.hasKey("TeslaInput"))
            this.inputRate = nbt.getLong("TeslaInput");

        if (nbt.hasKey("TeslaOutput"))
            this.outputRate = nbt.getLong("TeslaOutput");

        if (this.stored > this.getCapacity())
            this.stored = this.getCapacity();
    }

    /**
     * Sets the capacity of the the container. If the existing stored power is more than the
     * new capacity, the stored power will be decreased to match the new capacity.
     *
     * @param capacity The new capacity for the container.
     * @return The instance of the container being updated.
     */
    public BasicCable setCapacity (long capacity) {

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
    public BasicCable setInputRate (long rate) {

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
    public BasicCable setOutputRate (long rate) {

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
    public BasicCable setTransferRate (long rate) {

        this.setInputRate(rate);
        this.setOutputRate(rate);
        return this;
    }
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(heldItem != null && heldItem.getItem() instanceof ItemClock)
            this.breakBlock(worldIn,pos,state);
        return false;
    }
}
