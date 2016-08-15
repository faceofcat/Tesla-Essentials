package com.trials.modsquad.block.TileEntities;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.Recipies.TeslaRegistry;
import com.trials.modsquad.block.machines.ActiveState;
import com.trials.modsquad.block.machines.BlockElectricFurnace;
import com.trials.modsquad.proxy.TileDataSync;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
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
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class TileElectricFurnace extends TileEntity implements IItemHandlerModifiable, ITickable{

    private ItemStack[] inventory;
    private BaseTeslaContainer container;
    private boolean isSmelting = false;
    private int workTime = 0;
    static final int DRAW_PER_TICK = 10;

    private IItemHandlerModifiable extractor;
    private IItemHandlerModifiable inserter;

    public TileElectricFurnace(){
        container = new BaseTeslaContainer();
        inventory = new ItemStack[2];
        MinecraftForge.EVENT_BUS.register(this);
        extractor = new IItemHandlerModifiable() {
            @Override
            public void setStackInSlot(int slot, ItemStack stack) {
                TileElectricFurnace.this.setStackInSlot(slot, stack);
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
                return TileElectricFurnace.this.extractItem(slot, amount, simulate);
            }
        };
        inserter = new IItemHandlerModifiable() {
            @Override
            public void setStackInSlot(int slot, ItemStack stack) {
                TileElectricFurnace.this.setStackInSlot(slot, stack);
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
                return TileElectricFurnace.this.insertItem(slot, stack, simulate);
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
        compound.setBoolean("IsGrinding", isSmelting);
        compound.setInteger("GrindTime", workTime);
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
        isSmelting = compound.getBoolean("IsGrinding");
        workTime = compound.getInteger("GrindTime");

    }
    private int firstfewTicks = 500;

    @SubscribeEvent
    public void onEntityJoinEvent(EntityJoinWorldEvent event){
        firstfewTicks = 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void update() {
        if(firstfewTicks>=10 && firstfewTicks!=500 && !worldObj.isRemote){
            if(pos!=null) ModSquad.channel.sendToAll(new TileDataSync(5, pos, serializeNBT().toString()));
            firstfewTicks=500;
        }else if(firstfewTicks!=500) ++firstfewTicks;

        if(isSmelting){
            if(workTime==0){
                ItemStack s = extractItem(0, 1, false);
                if(s==null){ // Invalid state that for some reason can arise
                    isSmelting = false;
                    workTime = 0;
                    return;
                }
                if(inventory[1]==null){
                    inventory[1] = FurnaceRecipes.instance().getSmeltingResult(s).copy();
                }else inventory[1].stackSize+=FurnaceRecipes.instance().getSmeltingResult(s).stackSize;
                isSmelting = false;
            }
            if(container.getStoredPower()<DRAW_PER_TICK){
                isSmelting = false;
                workTime = 0;
                return;
            }
            container.takePower(DRAW_PER_TICK, false);
            --workTime;
        }else{
            ItemStack itemstack = inventory[0] != null ? FurnaceRecipes.instance().getSmeltingResult(inventory[0]) : null;
            int size;
            if (itemstack != null && (inventory[1] == null || inventory[1].isItemEqual(itemstack)) && (size = (inventory[1] != null ? inventory[1].stackSize : 0) + itemstack.stackSize) <= 64 &&
                    size <= itemstack.getMaxStackSize()) {
                isSmelting = true;
                workTime = 60;
            }
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack;
    }

    @Override
    public int getSlots() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
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
        if(isSmelting){
            isSmelting = false;
            workTime = 0;
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
            if(slot==0 && isSmelting){
                isSmelting = false;
                workTime = 0;
            }
            return split;
        }
        if(simulate) (split = inventory[slot].copy()).stackSize = amount;
        else split = inventory[slot].splitStack(amount);
        return split;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == CAPABILITY_HOLDER || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==CAPABILITY_HOLDER || capability==CAPABILITY_CONSUMER) return (T) container;
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            if(facing==EnumFacing.DOWN) return (T) extractor;
            else return (T) inserter;
        }
        return super.getCapability(capability, facing);
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
        isSmelting = compound.getBoolean("IsGrinding");
        workTime = compound.getInteger("GrindTime");
    }

    public void updateNBT(NBTTagCompound compound){ deserializeNBT(compound); }
}
