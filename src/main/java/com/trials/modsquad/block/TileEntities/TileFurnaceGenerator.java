package com.trials.modsquad.block.TileEntities;

import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;


public class TileFurnaceGenerator extends TileEntity implements IInventory, ITeslaProducer, ITeslaHolder, ITickable {


    private ItemStack fuel;
    private BaseTeslaContainer container;
    private int workTime;
    private boolean isBurning;

    public TileFurnaceGenerator(){
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
        return index==0?fuel:null;
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(index!=0) return null;
        ItemStack s = fuel;
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
        ItemStack s = fuel;
        fuel = null;
        return s;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if(index!=0) return;
        if(stack==null || fuel==null || (stack.getItem().equals(fuel.getItem()) && stack.stackSize+fuel.stackSize<=64)) fuel = stack;

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
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        switch (id){
            case 0:
                return workTime;
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id){
            case 0:
                workTime = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 5; //TODO: Update if more stuff is added
    }

    @Override
    public void clear() {
        workTime = 0;
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
        if(isBurning){
            container.givePower(20, false);
            if(workTime==0) isBurning = false;
            else --workTime;
        }
        else if(fuel!=null && TileEntityFurnace.isItemFuel(fuel)){ // Fixes bug where generator tears through fuel supply
            isBurning = true;
            workTime = TileEntityFurnace.getItemBurnTime(fuel);
            decrStackSize(0, 1);
        }
    }

    @Override
    public String getName() {
        return "modsquad.furnacegen";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

}
