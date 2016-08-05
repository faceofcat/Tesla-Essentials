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
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import javax.annotation.Nullable;

import java.lang.reflect.Field;

import static com.trials.modsquad.Ref.BlockReference.LEAD_CABLE;


@SuppressWarnings("unused")
public class BasicCable extends Block implements INBTSerializable<NBTTagCompound> {
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

    public BasicCable(String unloc, String reg) {

        this(unloc, reg, 5000, 50, 50);
    }

    public BasicCable(String unloc, String reg, long capacity, long input, long output) {

        this(unloc, reg, 0, capacity, input, output);
    }
    public BasicCable(String unloc, String reg, long power, long capacity, long input, long output) {
        super(Material.ANVIL);
        this.stored = power;
        this.capacity = capacity;
        this.inputRate = input;
        this.outputRate = output;
        setUnlocalizedName(unloc);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
    }

    public BasicCable(NBTTagCompound dataTag) {
        super(Material.ANVIL);
        this.deserializeNBT(dataTag);

        // Solve some issues with deserialization???
        try{
            Field f;

            f = Block.class.getDeclaredField("unlocalizedName");
            f.setAccessible(true);
            if(f.get(this)==null) setUnlocalizedName(LEAD_CABLE.getUnlocalizedName()); // Default cable type

            f = IForgeRegistryEntry.class.getDeclaredField("registryName");
            f.setAccessible(true);
            if(f.get(this)==null) setRegistryName(LEAD_CABLE.getRegistryName()); // Default registry name

            f = Block.class.getDeclaredField("displayOnCreativeTab");
            f.setAccessible(true);
            if(f.get(this) == null) setCreativeTab(Ref.tabModSquad);
        }catch(Exception ignored){}
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
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //clock: Wrench
        if(heldItem != null && heldItem.getItem() instanceof ItemClock) {
            this.breakBlock(worldIn,pos,state);
        }
        return false;
    }
}
