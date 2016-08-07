package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.Sys;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileSolarPanel extends TileEntity implements net.minecraft.util.ITickable {

    private BaseTeslaContainer solarContainer;

    public TileSolarPanel(){
        solarContainer = new BaseTeslaContainer(10000, 20, 20);
    }

    @Override
    public void update() {
        if(solarContainer.getStoredPower()>0) {
            int i = TeslaUtils.getConnectedCapabilities(CAPABILITY_CONSUMER, worldObj, pos).size();
            if(i==0) return;
            solarContainer.takePower(TeslaUtils.distributePowerToAllFaces(worldObj, pos, Math.min(solarContainer.getStoredPower() / i, solarContainer.getOutputRate()), false), false);
        }
        // Increase internal power supply
        if (worldObj.getTopSolidOrLiquidBlock(pos).getY() >= pos.getY() && solarContainer.getStoredPower() < solarContainer.getCapacity() -5 && worldObj.isDaytime() && !worldObj.isRaining()) solarContainer.givePower(5, false);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) { return capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == CAPABILITY_HOLDER; }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_HOLDER || capability==CAPABILITY_PRODUCER) return (T) solarContainer;
        return super.getCapability(capability, facing);
    }

}
