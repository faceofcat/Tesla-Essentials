package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.block.ModBlocks;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

@SuppressWarnings("unchecked")
public class TileCable extends TileEntity implements ITickable{

    private BaseTeslaContainer container;
    private List<ITeslaConsumer> gaveTo = new ArrayList<>();

    public TileCable(long input, long output) {
        container = new BaseTeslaContainer(0, 0, input, output){
            @Override
            public long givePower (long in, boolean simulated) {
                System.out.println("Given "+in+" tesla");
                long Tesla = Math.min(in, getOutputRate());
                List<ITeslaConsumer> avail = TeslaUtils.getConnectedCapabilities(TeslaCapabilities.CAPABILITY_CONSUMER, worldObj, pos);
                System.out.println("Available sides "+Arrays.toString(avail.toArray()));
                long tmp, requested = 0;
                for(ITeslaConsumer c : avail) requested+=c.givePower(Tesla, true);
                if(simulated) return Math.min(Tesla, requested);
                long returnVal, value = returnVal = requested<=Tesla?requested:Tesla;
                for (ITeslaConsumer c : avail) {
                    returnVal -= tmp = givePower(returnVal, false);
                    if (tmp != 0 && !gaveTo.contains(c)) gaveTo.add(c);
                    if (returnVal == 0) return value;
                }
                return 0;
            }
        };
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        TileEntity e;
        return (capability==CAPABILITY_PRODUCER || capability==CAPABILITY_CONSUMER) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        TileEntity e;
        if(capability==CAPABILITY_PRODUCER || capability==CAPABILITY_CONSUMER) return (T) container;
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() { gaveTo = new ArrayList<>(); } //Clear disallow-list
}
