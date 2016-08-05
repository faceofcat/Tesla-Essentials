package com.trials.modsquad.block.TileEntities;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;


public class TileCharger extends TileEntity implements IInventory, ITeslaProducer, ITeslaHolder, ITickable {


    private ItemStack item;
    private BaseTeslaContainer container;

    public TileCharger(){
        container = new BaseTeslaContainer();
    }

    @Override
    public long takePower(long power, boolean simulated) {
        return container.takePower(power, simulated);
    }

    @Override
    public long getStoredPower() {
        return container.getStoredPower();
    }

    @Override
    public long getCapacity() {
        return container.getCapacity();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return index==0?item:null;
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(index!=0) return null;
        ItemStack s = item;
        if(s!=null){
            if(s.stackSize <= count) setInventorySlotContents(index, null);
            else if((s = s.splitStack(count)).stackSize==0) setInventorySlotContents(index, null);
        }
        return s;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(index!=0) return null;
        ItemStack s = item;
        item = null;
        return s;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if(index!=0) return;
        if(stack==null || item==null || (stack.getItem().equals(item.getItem()) && stack.stackSize+item.stackSize<=64)) item = stack;

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5) < 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) { return 0; }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 5; //TODO: Update if more stuff is added
    }

    @Override
    public void clear() {
        try{
            Field f = BaseTeslaContainer.class.getDeclaredField("stored");
            f.setAccessible(true);
            f.setLong(container, 0);
        }catch(Exception e){}
        container.setCapacity(0);
        container.setInputRate(0);
        container.setOutputRate(0);

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
        System.out.println(item != null && item.getItem() instanceof ICapabilityProvider &&
                ((ICapabilityProvider) item.getItem()).hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN)?item+" has the capability!":item+" does not have the capability!");
    }

    @Override
    public String getName() {
        return "modsquad.charger";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

}
