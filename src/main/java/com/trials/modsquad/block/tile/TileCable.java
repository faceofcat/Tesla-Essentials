package com.trials.modsquad.block.tile;

import com.trials.modsquad.block.machine.BasicCable;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import java.util.*;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

@SuppressWarnings("ALL")
public class TileCable extends TileEntity implements ITickable{

    private BaseTeslaContainer container;
    boolean needUpdate = false;
    private Map<TileEntity, ObjectPair<EnumFacing, Long>> netList;
    private Map<TileEntity, EnumFacing> adjacentTiles = new HashMap<>(); // Faces are defined by the TileEntity. This allows for modification and extension past the six faces
    private TileEntity[] adjacent = new TileEntity[EnumFacing.VALUES.length];
    private boolean updatedForTick = false;

    public TileCable(){
        this(20, 20);
    }

    public TileCable(long input, long output) {
        container = new BaseTeslaContainer(0, 0, input, output){
            @Override
            public long givePower (long in, boolean simulated) {
                netList = buildNetwork(new ArrayList<>()); // Gets list of all connected machine
                if(netList.size()==0) return 0;
                // Since Cable doesn't have power storage, it relies on pushing the power to the next devices
                long actualOut = Math.min(in, Math.min(getOutputRate(), getInputRate())), equalSplit, tmp, tmp2, remainder, requestedPower = 0; for(TileEntity e : netList.keySet())
                    requestedPower+=e.getCapability(CAPABILITY_CONSUMER, netList.get(e).getKey()).givePower(actualOut, true);
                tmp2 = requestedPower = Math.min(requestedPower, actualOut);
                if(simulated) return requestedPower;
                equalSplit = requestedPower/netList.size();
                remainder = requestedPower%netList.size();

                for(int i = 0; i<2; ++i){ // Do two passes in case there is remainder from first pass (when machine rejects it's share of it's power)
                    if(requestedPower==0) break;
                    for(TileEntity t : netList.keySet()) {
                        requestedPower -= tmp = t.getCapability(CAPABILITY_CONSUMER, netList.get(t).getKey()).givePower(equalSplit + remainder, false);
                        remainder = (equalSplit+remainder)-tmp;
                    }
                }

                return tmp2-requestedPower; //In case not all of the power that SHOULD HAVE BEEN GIVEN WAS GIVEN YOU IDIOT I CAN'T BELIEVE IT I ASK YOU TO DO *ONE* THING I SWEAR TO GOD I'M OUT!
            }
        };
        needUpdate = true;
    }

    @Override
    public void setWorldObj(World worldIn) {
        super.setWorldObj(worldIn);
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
            else if (worldObj.getTileEntity(pos.offset(adjacentTiles.get(f)))==null ||
                    (worldObj.getTileEntity(pos.offset(adjacentTiles.get(f)))!=null && !worldObj.getTileEntity(pos.offset(adjacentTiles.get(f))).equals(f))) needUpdate = true;

        if(needUpdate){
            netList = buildNetwork(new ArrayList<>()); // Rebuild network of connected Tiles
            needUpdate = false;
        }

        // Does a specific check to see if updating the model is necessary. Only updates for client since graphics is clientside only
        if(worldObj.isRemote && needUpdate()) updateModel();

        updatedForTick = false;
    }

    public static <T> boolean arrayContains(T t, T[] ts){
        for(T t1 : ts) if(t.equals(t1)) return true;
        return false;
    }

    private Map<TileEntity, ObjectPair<EnumFacing, Long>> buildNetwork(List<TileCable> banList){
        long maxOut = Math.min(container.getOutputRate(), container.getInputRate());
        banList.add(this);
        List<TileCable> cables = new ArrayList<>();
        Map<TileEntity, ObjectPair<EnumFacing, Long>> capable = new HashMap<>();

        TileEntity e;
        adjacentTiles = new HashMap<>();
        for(EnumFacing f : EnumFacing.VALUES) if((e=worldObj.getTileEntity(pos.offset(f)))!=null) adjacentTiles.put(e, f);

        for(TileEntity t : adjacentTiles.keySet())
            if(t instanceof TileCable) cables.add((TileCable)t);
            else if(t.hasCapability(CAPABILITY_CONSUMER, adjacentTiles.get(t))) capable.put(t, new ObjectPair<>(adjacentTiles.get(t), maxOut));
        for(TileCable c : cables)
            if(!banList.contains(c)) {
                capable.putAll(c.buildNetwork(banList));
                // Update throughput rates to be compatible with this cable
                for(TileEntity e1 : capable.keySet()){
                    if(capable.get(e1).getValue()>maxOut) capable.get(e1).setValue(maxOut);
                }
            }
        return capable;
    }

    public boolean needUpdate(){
        boolean update = false;
        for(int i = 0; i<EnumFacing.VALUES.length; ++i){
            if(!update && adjacent[i]!=worldObj.getTileEntity(pos.offset(EnumFacing.VALUES[i]))) update=true;
            adjacent[i]=worldObj.getTileEntity(pos.offset(EnumFacing.VALUES[i]));
        }
        return update;
    }

    /**
     * Updates model to connect to adjacent, tesla-capable machine.
     */
    public void updateModel(){
        if(updatedForTick) return;
        BasicCable.updateModel(worldObj, pos, worldObj.getBlockState(pos), false);
    }

    public static final class ObjectPair<K, V>{

        K k;
        V v;

        public ObjectPair(K k, V v){
            this.k = k;
            this.v = v;
        }

        public K getKey(){ return k; }
        public V getValue(){ return v; }
        public void setKey(K k){ this.k = k; }
        public void setValue(V v){ this.v = v; }
    }
}
