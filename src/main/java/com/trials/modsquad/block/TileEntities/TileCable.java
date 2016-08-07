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

import java.util.*;
import java.util.stream.Collectors;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

@SuppressWarnings("unchecked")
public class TileCable extends TileEntity implements ITickable{

    private BaseTeslaContainer container;
    private boolean builtForTick = false;
    boolean needUpdate = false;
    private long rate;
    private List<TileEntity> netList;
    private Map<TileEntity, EnumFacing> adjacentTiles = new HashMap<>(); // Faces are defined by the TileEntity. This allows for modification and extension past the six faces

    public TileCable(){
        this(20, 20);
    }

    public TileCable(long input, long output) {
        container = new BaseTeslaContainer(0, 0, input, output){
            @Override
            public long givePower (long in, boolean simulated) {
                netList = getNetwork(new ArrayList<>()); // Gets list of all connected machines
                long actualOut = Math.min(in, Math.min(getOutputRate(), getInputRate())); //Since Cable doesn't have power storage, it relies on pushing the power to the next devices

                return 0;
            }
        };
        rate = Math.min(container.getOutputRate(), container.getInputRate());
        needUpdate = true;
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
        //Check for adjacent block changes
        for(EnumFacing f : EnumFacing.VALUES)
            if(worldObj.getTileEntity(pos.offset(f))!=null && !adjacentTiles.keySet().contains(worldObj.getTileEntity(pos.offset(f)))) {
                needUpdate = true; // Register a change has occurred
                break;
            }
        for(TileEntity f : adjacentTiles.keySet())
            if(needUpdate) break;
            else if (worldObj.getTileEntity(pos.offset(adjacentTiles.get(f)))==null || !worldObj.getTileEntity(pos.offset(adjacentTiles.get(f))).equals(f)) needUpdate = true;

        if(needUpdate){
            TileEntity e;
            adjacentTiles = new HashMap<>();
            for(EnumFacing f : EnumFacing.VALUES)
                if((e=worldObj.getTileEntity(pos.offset(f)))!=null)
                    adjacentTiles.put(e, f);
            netList = buildNetwork(new ArrayList<>()); // Rebuild network of connected Tiles
            needUpdate = false;
        }

    }

    /**
     * Should only be called when absolutely necessary! Use getNetwork() if at all possible!
     * Calling this method instead of getNetwork() is not only a bad idea tickrate-wise, but also stupid since getNetwork() does a check to see if this method needs to be called before it can return.
     * @param banList List of cables that future cables should ignore when searching for connected machines
     * @return a list of all tileEntities
     */
    private List<TileEntity> buildNetwork(List<TileCable> banList){
        System.out.println("Building network for "+pos);
        banList.add(this);
        List<TileCable> cables = new ArrayList<>();
        List<TileEntity> capable = new ArrayList<>();
        for(TileEntity t : adjacentTiles.keySet())
            if(t instanceof TileCable) cables.add((TileCable)t);
            else if(t.hasCapability(CAPABILITY_CONSUMER, adjacentTiles.get(t))) capable.add(t);
        for(TileCable c : cables)
            if(!banList.contains(c))
                capable.addAll(c.buildNetwork(banList));
        return capable;
    }

    public List<TileEntity> getNetwork(List<TileCable> banList){
        if(netList==null) netList = buildNetwork(banList);
        return netList;
    }

    private static <T> List<T> getCapable(Capability<T> c, BlockPos pos, List<TileEntity> e){
        List<T> l = new ArrayList<>();
        e.stream().filter(e1 -> e1 != null && e1.hasCapability(c, ModBlocks.getRelativeFace(e1.getPos(), pos))).forEach(e1 -> l.add(e1.getCapability(c, ModBlocks.getRelativeFace(e1.getPos(), pos))));
        return l;
    }
}
