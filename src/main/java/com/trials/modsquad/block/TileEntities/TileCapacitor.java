package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.proxy.TileDataSync;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

import static net.darkhax.tesla.capability.TeslaCapabilities.*;

public class TileCapacitor extends TileEntity implements ITickable {

    private BaseTeslaContainer container;

    public TileCapacitor () {
        container = new BaseTeslaContainer();
        container.setCapacity(200000);
        container.setInputRate(80);
        container.setOutputRate(80);
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
        compound.setTag("Container", container.serializeNBT());
        compound = super.writeToNBT(compound);
        if(pos!=null) ModSquad.channel.sendToAll(new TileDataSync(0, pos, compound.toString()));
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
        if(compound.hasKey("Container")) container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
    }

    private int firstfewTicks = 500;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onEntityJoinEvent(EntityJoinWorldEvent event){
        firstfewTicks = 0;
    }

    @Override
    public void update() {
        if(firstfewTicks>=10 && firstfewTicks!=500 && !worldObj.isRemote){
            if(pos!=null) ModSquad.channel.sendToAll(new TileDataSync(2, pos, serializeNBT().toString()));
            firstfewTicks=500;
        }else if(firstfewTicks!=500) ++firstfewTicks;
        if(container.getStoredPower()>0) {
            int i = TeslaUtils.getConnectedCapabilities(CAPABILITY_CONSUMER, worldObj, pos).size();
            if(i==0) return;
            container.takePower(TeslaUtils.distributePowerToAllFaces(worldObj, pos, Math.min(container.getStoredPower() / i, container.getOutputRate()), false), false);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability==CAPABILITY_PRODUCER ||
                super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability==CAPABILITY_PRODUCER)
            return (T) container;
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound c = super.serializeNBT();
        try{
            Field f = NBTTagCompound.class.getDeclaredField("tagMap");
            f.setAccessible(true);
            Map<String, NBTBase> r = (Map<String, NBTBase>) f.get(c);
            Map<String, NBTBase> m = (Map<String, NBTBase>) f.get(container);
            for(String s : m.keySet()) r.put(s, m.get(s)); // Move container tags to my tags
            f.set(c, r);
        }catch(Exception ignored){}
        return c;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("Container")) container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
    }

    //Used by TileDataSync class when updating NBT data
    @SuppressWarnings("unused")
    public void updateNBT(NBTTagCompound compound){ deserializeNBT(compound); }

}
