package com.trials.modsquad.block.tile;

import com.trials.modsquad.ModSquad;
import com.trials.net.TileDataSync;
import com.trials.net.Updatable;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;


public class TileCharger extends TileEntity implements IItemHandlerModifiable, ITickable, Updatable {


    private ItemStack[] inventory;
    private BaseTeslaContainer container;

    public TileCharger(){
        container = new BaseTeslaContainer();
        container.setTransferRate(60);
        inventory = new ItemStack[1];
        MinecraftForge.EVENT_BUS.register(this);
    }


    @Override
    public int getSlots() {
        return inventory.length;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack tmp;
        if(slot==1) return stack;
        if(inventory[slot] == null){
            if(!simulate) inventory[slot] = stack.copy();
            return null;
        }
        if(inventory[slot].isItemEqual(stack)){
            if(inventory[slot].stackSize+stack.stackSize<=64 && inventory[slot].stackSize+stack.stackSize<=stack.getMaxStackSize()) {
                if(!simulate) inventory[slot].stackSize += stack.stackSize;
                return null;
            }
            tmp = stack.copy();
            tmp.stackSize = stack.getMaxStackSize() - inventory[slot].stackSize - stack.stackSize;
            if(!simulate) inventory[slot].stackSize = stack.getMaxStackSize();
            return tmp;
        }
        if(simulate) return inventory[slot];
        tmp = inventory[slot];
        inventory[slot] = stack;
        return tmp;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack split;
        ITeslaHolder h;
        if(inventory[slot]==null || (inventory[0].hasCapability(CAPABILITY_HOLDER, EnumFacing.DOWN)) &&
                (h=inventory[0].getCapability(CAPABILITY_HOLDER, EnumFacing.DOWN)).getCapacity()<h.getStoredPower()) return null;
        if(amount>=inventory[slot].stackSize){
            split = inventory[slot];
            if(!simulate) inventory[slot] = null;
            return split;
        }
        if(simulate) (split = inventory[slot].copy()).stackSize = amount;
        else split = inventory[slot].splitStack(amount);
        return split;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER) return (T) container; // Not picky
        if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this; // Not picky
        return super.getCapability(capability, facing); // Possibly picky
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
        NBTTagList list = new NBTTagList();
        for(int i = 0; i<inventory.length; ++i)
            if(inventory[i]!=null){
                NBTTagCompound comp = new NBTTagCompound();
                comp.setInteger("Slot", i);
                inventory[i].writeToNBT(comp);
                list.appendTag(comp);
            }
        compound.setTag("Inventory", list);
        compound.setTag("Container", container.serializeNBT());
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
        if(compound.hasKey("Container")) container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
        NBTTagList list = compound.getTagList("Inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i<list.tagCount(); ++i){
            NBTTagCompound c = list.getCompoundTagAt(i);
            int slot = c.getInteger("Slot");
            if(slot>=0 && slot < inventory.length) inventory[slot] = ItemStack.loadItemStackFromNBT(c);
        }

    }
    private int syncTick = 0;

//    @SuppressWarnings("unused")
//    @SubscribeEvent
//    public void onEntityJoinEvent(EntityJoinWorldEvent event){
//        //NOP
//    }

    @Override
    public void update() {
        if (inventory[0] != null && inventory[0].hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN) &&
                inventory[0].hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN)) {
            ITeslaConsumer c = inventory[0].getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN);
            ITeslaHolder h = inventory[0].getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
            if (h.getStoredPower() < h.getCapacity()) container.takePower(c.givePower(Math.min(container.getOutputRate(), container.getStoredPower()), false), false);
        }
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
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack!=null?stack.copy():null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("Container")) container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
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
