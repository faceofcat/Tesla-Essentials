package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.recipe.TeslaRegistry;
import com.trials.modsquad.proxy.TileDataSync;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import javax.annotation.Nullable;
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
    private IItemHandlerModifiable extractor;
    private IItemHandlerModifiable inserter;

    public TileGrinder(){
        inventory = new ItemStack[2];
        container = new BaseTeslaContainer();
        container.setInputRate(100);
        MinecraftForge.EVENT_BUS.register(this);
        extractor = new IItemHandlerModifiable() {
            @Override
            public void setStackInSlot(int slot, ItemStack stack) {
                TileGrinder.this.setStackInSlot(slot, stack);
            }

            @Override
            public int getSlots() {
                return 1;
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return inventory[slot+1];
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return stack;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return TileGrinder.this.extractItem(slot+1, amount, simulate);
            }
        };
        inserter = new IItemHandlerModifiable() {
            @Override
            public void setStackInSlot(int slot, ItemStack stack) {
                TileGrinder.this.setStackInSlot(slot, stack);
            }

            @Override
            public int getSlots() {
                return 1;
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return inventory[slot];
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return TileGrinder.this.insertItem(slot, stack, simulate);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return null;
            }
        };
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
        compound.setBoolean("IsGrinding", isGrinding);
        compound.setInteger("GrindTime", grindTime);
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
        isGrinding = compound.getBoolean("IsGrinding");
        grindTime = compound.getInteger("GrindTime");

    }
    @Override
    public int getSlots() {
        return inventory!=null?inventory.length:0;
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
        if(isGrinding){ // If item are switched while grinding, grinding stops
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
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER)
            return (T) container;
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            if(facing==EnumFacing.DOWN) return (T) extractor;
            else return (T) inserter;
        }
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

        compound = super.writeToNBT(compound);
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

    private int firstfewTicks = 500;

    @SubscribeEvent
    public void onEntityJoinEvent(EntityJoinWorldEvent event){
        firstfewTicks = 0;
    }

    @Override
    public void update() {
        if(firstfewTicks>=10 && firstfewTicks!=500 && !worldObj.isRemote){
            if(pos!=null) ModSquad.channel.sendToAll(new TileDataSync(0, pos, serializeNBT().toString()));
            firstfewTicks=500;
        }else if(firstfewTicks!=500) ++firstfewTicks;
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

    public void updateNBT(NBTTagCompound compound){ deserializeNBT(compound); }

}
