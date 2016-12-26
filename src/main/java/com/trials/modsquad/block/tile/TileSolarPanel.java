package com.trials.modsquad.block.tile;

import com.trials.modsquad.ModSquad;
import com.trials.net.TileDataSync;
import com.trials.net.Updatable;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import javax.annotation.Nullable;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileSolarPanel extends TileEntity implements net.minecraft.util.ITickable, Updatable {

    private BaseTeslaContainer solarContainer;

    public TileSolarPanel(){
        solarContainer = new BaseTeslaContainer(0, 10000, 20, 20);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound t = new NBTTagCompound();
        t = writeToNBT(t);
        return new SPacketUpdateTileEntity(pos, 0, t);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("Container", solarContainer.serializeNBT());
        compound = super.writeToNBT(compound);
        if(pos!=null){
            int dim = 0;
            for(int i : DimensionManager.getIDs())
                if(DimensionManager.getWorld(i).equals(worldObj)) {
                    dim = i;
                    break;
                }
            ModSquad.channel.sendToAll(new TileDataSync(pos, compound.toString(), dim));
        }
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("Container")) solarContainer.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
    }
    private int syncTick = 10;

//    @SuppressWarnings("unused")
//    @SubscribeEvent
//    public void onEntityJoinEvent(EntityJoinWorldEvent event){
//        //NOP
//    }

    @Override
    public void update() {
        if(solarContainer.getStoredPower()>0) {
            int i = TeslaUtils.getConnectedCapabilities(CAPABILITY_CONSUMER, worldObj, pos).size();
            if(i!=0)
                solarContainer.takePower(TeslaUtils.distributePowerToAllFaces(worldObj, pos, Math.min(solarContainer.getStoredPower() / i, solarContainer.getOutputRate()), false), false);
        }
        // Increase internal power supply
        if (worldObj.getTopSolidOrLiquidBlock(pos).getY()-1 == pos.getY() && solarContainer.getStoredPower() < solarContainer.getCapacity() && worldObj.isDaytime()
                && !worldObj.isRaining()) solarContainer.givePower(Math.min(solarContainer.getCapacity()-solarContainer.getStoredPower(), 5), false); // Fills as much as possible
        // Sync after update
        if(syncTick==10 && !worldObj.isRemote){
            if(pos!=null){
                int dim = 0;
                for(int i : DimensionManager.getIDs())
                    if(DimensionManager.getWorld(i).equals(worldObj)) {
                        dim = i;
                        break;
                    }
                ModSquad.channel.sendToAll(new TileDataSync(pos, serializeNBT().toString(), dim));
            }
            syncTick = 0;
        }else if(syncTick<10) ++syncTick;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) { return facing==EnumFacing.DOWN && (capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == CAPABILITY_HOLDER); }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(facing==EnumFacing.DOWN && (capability==CAPABILITY_HOLDER || capability==CAPABILITY_PRODUCER)) //noinspection unchecked
            return (T) solarContainer;
        return super.getCapability(capability, facing);
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("Container")) solarContainer.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
    }

    @Override
    public void update(String s) {
        try {
            deserializeNBT(JsonToNBT.getTagFromJson(s));
        } catch (NBTException e) {
            e.printStackTrace();
        }
    }

}
