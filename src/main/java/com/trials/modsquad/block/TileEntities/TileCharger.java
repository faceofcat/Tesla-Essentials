package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.proxy.TileDataSync;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.TabCompleter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;


public class TileCharger extends TileEntity implements IItemHandlerModifiable, ITickable {


    private ItemStack[] inventory;
    private BaseTeslaContainer container;

    public TileCharger(){
        container = new BaseTeslaContainer();
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
        ItemStack s = inventory[slot];
        if(simulate) return s;
        inventory[slot] = stack;
        return s;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(simulate) return inventory[slot];
        ItemStack s = inventory[slot];
        if(s.stackSize-amount<=0) inventory[slot]=null;
        else inventory[slot] = s.splitStack(amount);
        return s;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

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
        NBTTagList list = compound.getTagList("Inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i<list.tagCount(); ++i){
            NBTTagCompound c = list.getCompoundTagAt(i);
            int slot = c.getInteger("Slot");
            if(slot>=0 && slot < inventory.length) inventory[slot] = ItemStack.loadItemStackFromNBT(c);
        }

    }
    private int firstfewTicks = 500;

    @SubscribeEvent
    public void onEntityJoinEvent(EntityJoinWorldEvent event){
        firstfewTicks = 0;
    }

    @Override
    public void update() {
        if(firstfewTicks>=10 && firstfewTicks!=500 && !worldObj.isRemote){
            if(pos!=null) ModSquad.channel.sendToAll(new TileDataSync(3, pos, serializeNBT().toString()));
            firstfewTicks=500;
        }else if(firstfewTicks!=500) ++firstfewTicks;
        if (inventory[0] != null && inventory[0].hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN) &&
                inventory[0].hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN)) {
            ITeslaConsumer c = inventory[0].getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN);
            ITeslaHolder h = inventory[0].getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
            if (h.getStoredPower() < h.getCapacity()) container.takePower(c.givePower(Math.min(container.getOutputRate(), container.getStoredPower()), false), false);
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack!=null?stack.copy():null;
    }

    public void updateNBT(NBTTagCompound compound){ deserializeNBT(compound); }
}
