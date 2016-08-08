package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.Recipies.GrinderRecipe;
import com.trials.modsquad.Recipies.TeslaRegistry;
import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.proxy.TileDataSync;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class TileGrinder extends TileEntity implements IItemHandlerModifiable, ITickable {
    // Primitives
    private int grindTime;
    public static final int DEFAULT_GRIND_TIME = 60;
    public static final int DRAW_PER_TICK = 10;
    private boolean isGrinding = false;

    // Objects
    private final ItemStack[] inventory; // I'm an idiot
    private BaseTeslaContainer container;
    private ThreadLocalRandom random = ThreadLocalRandom.current();

    public TileGrinder(){
        inventory = new ItemStack[2];
        container = new BaseTeslaContainer();
        container.setInputRate(100);
    }


    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound t = new NBTTagCompound();
        writeToNBT(t);
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
        compound.setBoolean("IsGrinding", isGrinding);
        compound.setInteger("GrindTime", grindTime);
        compound.setTag("Container", container.serializeNBT());
        System.out.println("Writing to nbt: "+compound);
        return super.writeToNBT(compound);
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
        isGrinding = compound.getBoolean("IsGrinding");
        grindTime = compound.getInteger("GrindTime");

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
            inventory[slot] = stack.copy();
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
        if(isGrinding){ // If items are switched while grinding, grinding stops
            isGrinding = false;
            grindTime = 0;
        }
        return tmp;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack split;
        if(inventory[slot]==null) return null;
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
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER)
            return (T) container;
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for(int i = 0; i<inventory.length; ++i)
            if(inventory[i]!=null){
                NBTTagCompound comp = new NBTTagCompound();
                comp.setInteger("Slot", i);
                inventory[i].writeToNBT(comp);
                list.appendTag(comp);
            }
        compound.setTag("Inventory", list);
        compound.setBoolean("IsGrinding", isGrinding);
        compound.setInteger("GrindTime", grindTime);
        compound.setTag("Container", container.serializeNBT());

        compound =  super.writeToNBT(compound);

        ModSquad.channel.sendToAll(new TileDataSync(0, pos, compound.toString()));
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("Container")) container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
        NBTTagList list = compound.getTagList("Inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i<list.tagCount(); ++i){
            NBTTagCompound c = list.getCompoundTagAt(i);
            int slot = c.getInteger("Slot");
            if(slot>=0 && slot < inventory.length) inventory[slot] = ItemStack.loadItemStackFromNBT(c);
        }
        isGrinding = compound.getBoolean("IsGrinding");
        grindTime = compound.getInteger("GrindTime");
    }

    @Override
    public void update() {
        if(isGrinding){
            if(grindTime==0){
                ItemStack s = extractItem(0, 1, false);
                if(s==null){ // Invalid state that for some reason can arise
                    isGrinding = false;
                    return;
                }
                if(inventory[1]==null){
                    inventory[1] = TeslaRegistry.teslaRegistry.getGrinderOutFromIn(s).copy();
                    inventory[1].stackSize = TeslaRegistry.teslaRegistry.getGrinderRecipeFromIn(s).getAmount();
                }else inventory[1].stackSize+=TeslaRegistry.teslaRegistry.getGrinderRecipeFromIn(s).getAmount();
                isGrinding = false;
            }
            if(container.getStoredPower()<DRAW_PER_TICK){
                isGrinding = false;
                grindTime = 0;
                return;
            }
            container.takePower(DRAW_PER_TICK, false);
            --grindTime;
        }else if(inventory[0]!=null && TeslaRegistry.teslaRegistry.hasRecipe(inventory[0]) && (inventory[1] == null ||
                inventory[1].isItemEqual(TeslaRegistry.teslaRegistry.getGrinderOutFromIn(inventory[0]))) && container.getStoredPower()>0 &&
                (inventory[1]==null || inventory[1].stackSize<64)){
            isGrinding = true;
            grindTime = DEFAULT_GRIND_TIME;
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack!=null?stack.copy():null;
    }

    public void updateNBT(NBTTagCompound compound){ System.out.println("Updating"); deserializeNBT(compound); }

    private int[] f = new int[2];

    public int getFieldCount(){
        return f.length;
    }

    public int getField(int i){
        return f[i];
    }

    public void setField(int i, int j){
        System.out.println(i+" "+j);
        f[i] = j;
    }
}
