package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileSolarPanel extends TileEntity implements ITickable {

    private BaseTeslaContainer solarContainer;

    public TileSolarPanel(){
        solarContainer = new BaseTeslaContainer();
    }

    @Override
    public void tick() {
        if(solarContainer.getStoredPower()>0) {
            int i = TeslaUtils.getConnectedCapabilities(CAPABILITY_CONSUMER, worldObj, pos).size();
            System.out.println(this+" found "+i+" capable side"+(i>1||i==0?"s.":"."));
            if(i==0) return;
            solarContainer.takePower(TeslaUtils.distributePowerToAllFaces(worldObj, pos, Math.min(solarContainer.getStoredPower() / i, solarContainer.getOutputRate()), false), false);
        }
    }



    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) { return capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == CAPABILITY_HOLDER; }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_HOLDER || capability==CAPABILITY_PRODUCER) return (T) solarContainer;
        return super.getCapability(capability, facing);
    }

}
