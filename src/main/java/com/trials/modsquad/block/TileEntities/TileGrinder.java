package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.Recipies.GrinderRecipe;
import com.trials.modsquad.Recipies.TeslaRegistry;
import com.trials.modsquad.block.ModBlocks;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.trials.modsquad.block.ModBlocks.getRelativeFace;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileGrinder extends TileEntity implements IInventory, ITeslaConsumer, ITeslaHolder, ITickable, ISidedInventory {
    // Primitives
    private int workTime = 0;
    private int grindTime;
    public static final int DEFAULT_GRIND_TIME = 60;
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

    @Override
    public long givePower(long power, boolean simulated) {
        return container.givePower(power ,simulated);
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
        return inventory.length;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack s = inventory[index];
        if(s!=null){
            if(s.stackSize <= count) setInventorySlotContents(index, null);
            else if((s = s.splitStack(count)).stackSize==0) setInventorySlotContents(index, null);
        }
        return s;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack s = inventory[index];
        inventory[index] = null;
        return s;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if(index == 1) return;
        if(stack == null || inventory[index]==null){
            inventory[index] = stack;
            return;
        }
        if(stack.getItem().equals(inventory[index].getItem()) &&
                (inventory[0]==null ||
                        stack.stackSize+inventory[index].stackSize<=64)) {
            inventory[index] = stack;
        }

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
        // NOP
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        // NOP
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
            case 1:
                try{
                    Field f = BaseTeslaContainer.class.getDeclaredField("stored");
                    f.setAccessible(true);
                    f.setLong(container, value);
                }catch(Exception e){}
                break;
            case 2:
                container.setCapacity(value);
                break;
            case 3:
                container.setInputRate(value);
                break;
            case 4:
                container.setOutputRate(value);
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
        try{
            Field f = NBTTagCompound.class.getDeclaredField("tagMap");
            f.setAccessible(true);
            Map<String, NBTBase> r = (Map<String, NBTBase>) f.get(c);
            Map<String, NBTBase> m = (Map<String, NBTBase>) f.get(container);
            for(String s : m.keySet()) r.put(s, m.get(s)); // Move container tags to my tags
            f.set(c, r);
        }catch(Exception ignored){}
        return c;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        container.deserializeNBT(nbt);
    }

    @Override
    public String getName() {
        return "modsquad.tilegrinder";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void update() {
        if(isGrinding){
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, pos.getX(), pos.getY()+1, pos.getZ(),
                    random.nextGaussian() * .05, random.nextGaussian() * .05+.2,random.nextGaussian() * .05);
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, pos.getX(), pos.getY()+1, pos.getZ(),
                    random.nextGaussian() * .05, random.nextGaussian() * .05+.2,random.nextGaussian() * .05);
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, pos.getX(), pos.getY()+1, pos.getZ(),
                    random.nextGaussian() * .05, random.nextGaussian() * .05+.2,random.nextGaussian() * .05);
            if(grindTime == 0){
                //TODO: Update texture
                try{ //Troll tj
                    Field f = Class.forName("com.trials.modsquad.Recipes.TeslaRegistry.TeslaCraftingHandler").getDeclaredField("grinderRecipeList");
                    f.setAccessible(true);
                    //noinspection unchecked
                    for(GrinderRecipe g : (List<GrinderRecipe>) f.get(TeslaRegistry.teslaRegistry))
                        if(g.getInput().getItem().equals(inventory[0].getItem()) &&
                                (inventory[1]==null || (inventory[1].getItem().equals(g.getOutput()) &&
                                        g.getAmount()+inventory[1].stackSize<=64))){
                            ItemStack i = new ItemStack(g.getOutput().getItem(), inventory[1].stackSize+g.getAmount());
                            if(g.getOutput().hasTagCompound() && g.getOutput().getTagCompound()!=null)
                                i.setTagCompound(g.getOutput().getTagCompound());
                            inventory[1] = i;
                        }else return;

                }catch(Exception ignored){ System.out.println("SOMETHING WENT EXTREMELY WRONG! GO MAKE SURE YOUR HOUSE ISN'T ON FIRE!!!"); }
            }else --grindTime;
        }else if(inventory[0]!=null){
            try{
                Field f = Class.forName("com.trials.modsquad.Recipes.TeslaRegistry.TeslaCraftingHandler").getDeclaredField("grinderRecipeList");
                f.setAccessible(true);
                List<GrinderRecipe> r = ((List<GrinderRecipe>) f.get(TeslaRegistry.teslaRegistry));
                System.out.println("Raw tesla registry: "+r);
                for(GrinderRecipe g : r)
                    if(g.getInput().getItem().equals(inventory[0].getItem())){
                        isGrinding = true;
                        grindTime = DEFAULT_GRIND_TIME;
                        System.out.println(pos+" started grinding!");
                        //TODO: Update texture
                    }
            }catch(Exception e){} // Stupid exception that will never be thrown
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index==0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index==1;
    }
}
