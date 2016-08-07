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
    private boolean builtForTick = false;
    private List<TileEntity> netList;

    public TileCable(long input, long output) {
        container = new BaseTeslaContainer(0, 0, input, output){
            @Override
            public long givePower (long in, boolean simulated) {
                if(netList==null) netList = buildNetwork(new ArrayList<>());
                long Tesla = Math.min(in, getOutputRate());
                long tmp, requested = 0;
                List<ITeslaConsumer> cons = getCapable(CAPABILITY_CONSUMER, pos, netList);
                for(ITeslaConsumer c : cons) requested+=c.givePower(Tesla, true);
                if(simulated) return Math.min(Tesla, requested);
                long returnVal, value = returnVal = Math.min(requested, Tesla);
                for (ITeslaConsumer c : cons)
                    if ((returnVal -= c.givePower(returnVal, false)) == 0) return value;
                return returnVal;
            }
        };
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (capability==CAPABILITY_PRODUCER || capability==CAPABILITY_CONSUMER) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_PRODUCER || capability==CAPABILITY_CONSUMER) return (T) container;
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        builtForTick = false;
        netList = buildNetwork(new ArrayList<>()); // Rebuild network of connected Tiles
    }

    public List<TileEntity> buildNetwork(List<TileCable> banList){
        if(builtForTick && netList!=null) return netList;
        banList.add(this);
        List<TileCable> adj = new ArrayList<>();
        List<TileEntity> capable = new ArrayList<>();
        for(EnumFacing f : EnumFacing.VALUES)
            if(worldObj.getTileEntity(pos.offset(f)) instanceof TileCable)
                adj.add((TileCable) worldObj.getTileEntity(pos.offset(f)));
            else capable.add(worldObj.getTileEntity(pos.offset(f)));
        for(TileCable c : adj)
            if(!banList.contains(c))
                capable.addAll(c.buildNetwork(banList));
        builtForTick = true;
        return capable;
    }

    private static <T> List<T> getCapable(Capability<T> c, BlockPos pos, List<TileEntity> e){
        List<T> l = new ArrayList<>();
        for(TileEntity e1 : e)
            if(e1!=null && e1.hasCapability(c, ModBlocks.getRelativeFace(e1.getPos(), pos)))
                l.add(e1.getCapability(c, ModBlocks.getRelativeFace(e1.getPos(), pos)));
        return l;
    }
}
