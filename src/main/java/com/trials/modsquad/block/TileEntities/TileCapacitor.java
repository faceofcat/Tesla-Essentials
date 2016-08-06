package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.lang.reflect.Field;
import java.util.Map;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileCapacitor extends TileEntity {

    private BaseTeslaContainer container;

    public TileCapacitor () {
        container = new BaseTeslaContainer();
        container.setCapacity(200000);
        container.setInputRate(80);
        container.setOutputRate(80);
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
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        container.deserializeNBT(nbt);
    }

}
