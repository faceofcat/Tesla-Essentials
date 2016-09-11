package com.trials.modsquad.block.tile;

import com.trials.modsquad.item.ModItems;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class TileToaster extends TileEntity {

    private boolean slot1, slot2 = false;
    private boolean toast1, toast2 = false;
    private boolean isToasting = true;
    private int toastTimeRemaining = 20;

    private BaseTeslaContainer container;

    public TileToaster() {
        container = new BaseTeslaContainer();
        container.setTransferRate(60);
    }

    public boolean addSlot1() {
        if (!worldObj.isRemote) {
            IBlockState state = worldObj.getBlockState(pos);
            if (!slot1) {
                slot1 = true;
                toast1 = false;
                markDirty();
                worldObj.notifyBlockUpdate(pos,state,state,3);
                return true;
            } else if (!slot2) {
                slot2 = true;
                toast2 = false;
                markDirty();
                worldObj.notifyBlockUpdate(pos,state,state,3);
                return true;
            }
        }
        return false;
    }

    public void removeSlot1() {
        if (!worldObj.isRemote) {
            IBlockState state = worldObj.getBlockState(pos);
            if (slot2) {
                Item dropItem;
                if (toast2) {dropItem = ModItems.toastSlice;} else {dropItem = ModItems.breadSlice;}
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX()+.5, pos.getY()+1, pos.getZ()+.5, new ItemStack(dropItem)));
                slot2 = false;
                toast2 = false;
                markDirty();
                worldObj.notifyBlockUpdate(pos,state,state,3);
            } else if(slot1) {
                Item dropItem;
                if (toast1) {dropItem = ModItems.toastSlice;} else {dropItem = ModItems.breadSlice;}
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX()+.5, pos.getY()+1, pos.getZ()+.5, new ItemStack(dropItem)));
                slot1 = false;
                toast1 = false;
                markDirty();
                worldObj.notifyBlockUpdate(pos,state,state,3);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("slot1", slot1);
        compound.setBoolean("slot2", slot2);
        compound.setBoolean("toast1", toast1);
        compound.setBoolean("toast2", toast2);
        compound.setInteger("toastTimeRemaining", toastTimeRemaining);
        compound.setTag("Container", container.serializeNBT());
        compound.setBoolean("isToasting", isToasting);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.slot1 = compound.getBoolean("slot1");
        this.slot2 = compound.getBoolean("slot2");
        this.toast1 = compound.getBoolean("toast1");
        this.toast2 = compound.getBoolean("toast2");
        this.toastTimeRemaining = compound.getInteger("toastTimeRemaining");
        if(compound.hasKey("Container")) container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
        this.isToasting = compound.getBoolean("isToasting");
    }

    public boolean getSlot1() {return this.slot1;}
    public boolean getSlot2() {return this.slot2;}
    public boolean getToast1() {return this.toast1;}
    public boolean getToast2() {return this.toast2;}
    public int getToastTimeRemaining() {return this.toastTimeRemaining;}
    public boolean isToasting() {return this.isToasting;}
    public long getStoredPower() {
        return container.getStoredPower();
    }
    public void toast() {
        if (slot1 && !toast1) {
            toast1 = true;
        }
        if (slot2 && !toast2) {
            toast2 = true;
        }
        markDirty();
        IBlockState state = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos,state,state,3);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER) return (T) container; // Not picky
        if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this; // Not picky
        return super.getCapability(capability, facing); // Possibly picky
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        readUpdateTag(tag);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeUpdateTag(tag);
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tag);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        writeUpdateTag(tag);
        return tag;
    }

    public void writeUpdateTag(NBTTagCompound tag) {
        tag.setBoolean("slot1", slot1);
        tag.setBoolean("slot2", slot2);
        tag.setBoolean("toast1", toast1);
        tag.setBoolean("toast2", toast2);
        tag.setInteger("toastTimeRemaining", toastTimeRemaining);
        tag.setBoolean("isToasting", isToasting);
    }

    public void readUpdateTag(NBTTagCompound tag) {
        this.slot1 = tag.getBoolean("slot1");
        this.slot2 = tag.getBoolean("slot2");
        this.toast1 = tag.getBoolean("toast1");
        this.toast2 = tag.getBoolean("toast2");
        this.toastTimeRemaining = tag.getInteger("toastTimeRemaining");
        this.isToasting = tag.getBoolean("isToasting");
    }

}
