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
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
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


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //clock: Wrench
        if(heldItem != null && heldItem.getItem() instanceof ItemClock) {
            this.breakBlock(worldIn,pos,state);
        }
        //Door: Reader
        if(heldItem != null && heldItem.getItem() instanceof ItemDoor) {
            System.out.println(getStoredPower());
        }
        return false;
    }
}
