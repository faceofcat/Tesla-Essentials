package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.block.ModBlocks;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileCable extends TileEntity implements ITeslaHolder, ITeslaConsumer, ITickable {

    private BaseTeslaContainer container;

    int x;
    int y;
    int z;

    public TileCable() {

        this(5000, 50, 50);
    }

    public TileCable(long capacity, long input, long output) {

        this(0, capacity, input, output);
    }

    public TileCable(long power, long capacity, long input, long output) {
        container = new BaseTeslaContainer(power, capacity, input, output);
    }

    @Override
    public long getStoredPower () {

        return container.getStoredPower();
    }

    @Override
    public long givePower (long Tesla, boolean simulated) {
        return container.givePower(Tesla, simulated);
    }

    @Override
    public long getCapacity () {

        return container.getCapacity();
    }

    public TileCable setCapacity (long capacity) {
        container.setCapacity(capacity);
        return this;
    }

    public long getInputRate () {

        return container.getInputRate();
    }

    public TileCable setInputRate (long rate) {

        container.setInputRate(rate);
        return this;
    }

    public long getOutputRate () {

        return container.getOutputRate();
    }

    public TileCable setOutputRate (long rate) {

        container.setOutputRate(rate);
        return this;
    }

    public TileCable setTransferRate (long rate) {

        this.setInputRate(rate);
        this.setOutputRate(rate);
        return this;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CAPABILITY_PRODUCER || capability == CAPABILITY_HOLDER;
    }

    @Override
    public void update() {
        if(!worldObj.isRemote){
            if(getStoredPower() < getCapacity()){
                x = this.getPos().getX();
                y = this.getPos().getY();
                z = this.getPos().getZ();
                TileEntity e;
                if((e=worldObj.getTileEntity(new BlockPos(x,y+1,z)))!=null && e.hasCapability(CAPABILITY_PRODUCER, ModBlocks.getRelativeFace(pos, e.getPos()).getOpposite()))
                    givePower(((ITeslaProducer) e).takePower(Math.min(getInputRate(), getCapacity()-getStoredPower()), false), false);
            }

        }
    }
}
