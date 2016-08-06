package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.block.ModBlocks;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;


public class TileFurnaceGenerator extends TileEntity implements IItemHandlerModifiable, ITickable, ICapabilityProvider{


    private BlockPos[] MySidesAreInOrbitxDDDDDDDDTopKek = new BlockPos[]{
            new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()),
            new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()),
            new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()),
            new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()),
            new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1),
            new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)
    };
    private ItemStack[] fuel;
    private BaseTeslaContainer container;

    private int workTime;
    private boolean isBurning;


    public TileFurnaceGenerator(){
        container = new BaseTeslaContainer();
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return fuel[index];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return null;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
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
        TileEntity t;
        if(isBurning){
            container.givePower(20, false);
            if(workTime==0) isBurning = false;
            else --workTime;
        }
        else if(fuel!=null && TileEntityFurnace.isItemFuel(fuel[0])){ // Fixes bug where generator tears through fuel supply
            isBurning = true;
            workTime = TileEntityFurnace.getItemBurnTime(fuel[0])/2; // Automatically casts away eventual decimal points
            decrStackSize(0, 1);
        }
        if(getStoredPower()>0)
            for(BlockPos side : MySidesAreInOrbitxDDDDDDDDTopKek){
                if((t=worldObj.getTileEntity(side))!=null && t.hasCapability(CAPABILITY_CONSUMER, ModBlocks.getRelativeFace(t.getPos(), pos)))
                    container.takePower(((ITeslaConsumer) t.getCapability(CAPABILITY_CONSUMER, ModBlocks.getRelativeFace(t.getPos(), pos)))
                            .givePower(Math.min(container.getOutputRate(), container.getStoredPower()), false), false);
            }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) { return capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == TeslaCapabilities.CAPABILITY_HOLDER; }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        fuel[slot] = stack;
    }
}
