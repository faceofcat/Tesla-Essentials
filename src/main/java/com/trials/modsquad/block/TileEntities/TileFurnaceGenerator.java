package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.Constants;
import net.darkhax.tesla.lib.TeslaUtils;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandlerModifiable;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import static net.darkhax.tesla.capability.TeslaCapabilities.*;


@SuppressWarnings("unchecked")
public class TileFurnaceGenerator extends TileEntity implements IItemHandlerModifiable, ITickable, ICapabilityProvider{

    private ItemStack[] fuel;
    private BaseTeslaContainer container;

    private int workTime;
    private boolean isBurning;

    public TileFurnaceGenerator(){
        container = new BaseTeslaContainer();
        fuel = new ItemStack[1];
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
        NBTTagCompound c = container.serializeNBT();
        for(String key : c.getKeySet()) compound.setTag(key, c.getTag(key));
        NBTTagList list = new NBTTagList();
        for(int i = 0; i<fuel.length; ++i)
            if(fuel[i]!=null){
                NBTTagCompound comp = new NBTTagCompound();
                comp.setInteger("Slot", i);
                fuel[i].writeToNBT(comp);
                list.appendTag(comp);
            }
        compound.setTag("Inventory", list);
        return super.writeToNBT(compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        container.deserializeNBT(compound);
        super.readFromNBT(compound);
        NBTTagList list = compound.getTagList("Inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i<list.tagCount(); ++i){
            NBTTagCompound c = list.getCompoundTagAt(i);
            int slot = c.getInteger("Slot");
            if(slot>=0 && slot < fuel.length) fuel[slot] = ItemStack.loadItemStackFromNBT(c);
        }

    }

    @Override
    public int getSlots() {
        return fuel.length;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return fuel[index];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack tmp;
        if(fuel[slot] == null){
            fuel[slot] = stack.copy();
            return null;
        }
        if(fuel[slot].isItemEqual(stack)){
            if(fuel[slot].stackSize+stack.stackSize<=64 && fuel[slot].stackSize+stack.stackSize<=stack.getMaxStackSize()) {
                if(!simulate) fuel[slot].stackSize += stack.stackSize;
                return null;
            }
            tmp = stack.copy();
            tmp.stackSize = stack.getMaxStackSize() - fuel[slot].stackSize - stack.stackSize;
            if(!simulate) fuel[slot].stackSize = stack.getMaxStackSize();
            return tmp;
        }
        if(simulate) return fuel[slot];
        tmp = fuel[slot];
        fuel[slot] = stack;
        return tmp;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack split;
        if(fuel[slot]==null) return null;
        if(amount>=fuel[slot].stackSize){
            split = fuel[slot];
            if(!simulate) fuel[slot] = null;
            return split;
        }
        if(simulate) (split = fuel[slot].copy()).stackSize = amount;
        else split = fuel[slot].splitStack(amount);
        return split;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound c = super.serializeNBT();
        System.out.println("Serializing");
        try{
            Field f = NBTTagCompound.class.getDeclaredField("tagMap");
            f.setAccessible(true);
            Map<String, NBTBase> r = (Map<String, NBTBase>) f.get(c);
            Map<String, NBTBase> m = (Map<String, NBTBase>) f.get(container);
            for(String s : m.keySet()){
                System.out.println("Ser: "+s);
                r.put(s, m.get(s)); // Move container tags to my tags
            }
            f.set(c, r);
        }catch(Exception ignored){}
        return c;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        System.out.println("Deserializing");
        super.deserializeNBT(nbt);
        container.deserializeNBT(nbt);
        System.out.println(container.getStoredPower()+" "+nbt.toString());
    }

    @Override
    public void update() {
        if(isBurning){
            container.givePower(20, false);
            if(workTime==0) isBurning = false;
            else --workTime;
        }
        else if(fuel!=null && TileEntityFurnace.isItemFuel(fuel[0])){ // Fixes bug where generator tears through fuel supply
            isBurning = true;
            workTime = (int) (TileEntityFurnace.getItemBurnTime(fuel[0])*0.625); // Automatically casts away eventual decimal points
            extractItem(0, 1, false);
        }
        if(container.getStoredPower()>0) {
            int i = TeslaUtils.getConnectedCapabilities(CAPABILITY_CONSUMER, worldObj, pos).size();
            if(i==0) return;
            container.takePower(TeslaUtils.distributePowerToAllFaces(worldObj, pos, Math.min(container.getStoredPower() / i, container.getOutputRate()), false), false);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == CAPABILITY_HOLDER || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_HOLDER || capability==CAPABILITY_PRODUCER) return (T) container;
        return super.getCapability(capability, facing);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        fuel[slot] = stack!=null?stack.copy():null;
    }
}
