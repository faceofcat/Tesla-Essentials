package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.block.Network.BasicCable;
import javafx.geometry.Side;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

@SuppressWarnings("ALL")
public class TileCable extends TileEntity implements ITickable{

    private BaseTeslaContainer container;
    boolean needUpdate = false;
    private Map<TileEntity, ObjectPair<EnumFacing, Long>> netList;
    private Map<TileEntity, EnumFacing> adjacentTiles = new HashMap<>(); // Faces are defined by the TileEntity. This allows for modification and extension past the six faces
    private DFacing connectedFaces = new DFacing();
    private DFacing pointing = new DFacing();

    public TileCable(){
        this(20, 20);
    }

    public TileCable(long input, long output) {
        container = new BaseTeslaContainer(0, 0, input, output){
            @Override
            public long givePower (long in, boolean simulated) {
                netList = buildNetwork(new ArrayList<>()); // Gets list of all connected machines
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

        //Possible fix b-cuz arraylist check seems to give OutOfBoundsException
        if(worldObj.loadedTileEntityList==null || (worldObj.loadedTileEntityList==null && worldObj.loadedTileEntityList.size()==0)) return capable;

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

    /**
     * Rotationally and cardinally ambiguous model update algorithm.
     */
    public void updateModel(){
        boolean flat = true;
        int rY = 0;
        List<EnumFacing> connections = new ArrayList<>();
        TileEntity e;
        for(EnumFacing f : EnumFacing.VALUES) {
            if((e=worldObj.getTileEntity(pos.offset(f)))!=null && (e.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, f.getOpposite()) ||
                    e.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, f.getOpposite()))){
                connections.add(f);
                if(f == EnumFacing.SOUTH) rY = 3;
            }
        }
        worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BasicCable.Tx, BasicCable.Type.fromData(connections.size(), flat))
                .withProperty(BasicCable.rY, BasicCable.Rotation.fromValue(rY)));
    }

    public static final class ObjectPair<K, V>{

        K k;
        V v;

        public ObjectPair(Object... o){
            k = (K) o[0];
            v = (V) o[1];
        }

        public ObjectPair(K k, V v){
            this.k = k;
            this.v = v;
        }

        public K getKey(){ return k; }
        public V getValue(){ return v; }
        public void setKey(K k){ this.k = k; }
        public void setValue(V v){ this.v = v; }
    }

    public static final class DFacing{
        private EnumSide[] mappedSides = new EnumSide[6]; // Generic mapping for storage
        private boolean[] activeSides = new boolean[6];
        public DFacing(){
            mappedSides[0] = EnumSide.Left;     // North
            mappedSides[1] = EnumSide.Right;    // South
            mappedSides[2] = EnumSide.Front;    // East
            mappedSides[3] = EnumSide.Back;     // West
            mappedSides[4] = EnumSide.Top;      // Up
            mappedSides[5] = EnumSide.Bottom;   // Down
        }

        public DFacing(boolean... active){
            this();
            if(active==null) return;
            for(int i = 0; i<Math.min(active.length, 6); ++i) activeSides[i] = active[i];
        }

        public DFacing(EnumSide... mappedSides){

        }

        /**
         * Rotates relative faces around X-axis.
         * @param times the amount of times to perform rotation modulo 4
         */
        public void rotateX(int times){
            EnumSide overflow = mappedSides[5];
            mappedSides[5] = mappedSides[1];
            mappedSides[1] = mappedSides[4];
            mappedSides[4] = mappedSides[0];
            mappedSides[0] = overflow;
            if((times%4)<0) rotateX((times%4)-1); // 4 rotations == 0 rotations
        }

        /**
         * Rotates relative faces around Y-axis.
         * @param times the amount of times to perform rotation modulo 4
         */
        public void rotateY(int times){
            EnumSide overflow = mappedSides[0];
            mappedSides[0] = mappedSides[2];
            mappedSides[2] = mappedSides[1];
            mappedSides[1] = mappedSides[3];
            mappedSides[3] = overflow;
            if((times%4)<0) rotateY((times%4)-1); // 4 rotations == 0 rotations
        }

        /**
         * Rotates relative faces around Z-axis.
         * @param times the amount of times to perform rotation modulo 4
         */
        public void rotateZ(int times){
            EnumSide overflow = mappedSides[5];
            mappedSides[5] = mappedSides[3];
            mappedSides[3] = mappedSides[4];
            mappedSides[4] = mappedSides[2];
            mappedSides[2] = overflow;
            if((times%4)<0) rotateZ((times%4)-1); // 4 rotations == 0 rotations
        }
    }

    /**
     * Like EnumFacing except relative!
     * This means that all these values are independent of a cardinal direction and can, as such, change cardinal direction at any time.
     * This means that BlockState rotation can be done separately from the cardinal directions.
     */
    public static enum EnumSide{
        Top, Bottom, Left, Right, Front, Back;
        public EnumSide getOpposite(){ return ordinal()%2==0?EnumSide.values()[ordinal()+1]:EnumSide.values()[ordinal()-1]; }
    }
}
