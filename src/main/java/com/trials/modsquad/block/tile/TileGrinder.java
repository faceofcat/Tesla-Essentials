package com.trials.modsquad.block.tile;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.States;
import com.trials.modsquad.recipe.TeslaRegistry;
import com.trials.net.TileDataSync;
import com.trials.net.Updatable;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
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

import static com.trials.modsquad.block.machine.BlockElectricFurnace.STATE;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class TileGrinder extends TileEntity implements IItemHandlerModifiable, ITickable, Updatable {
    // Primitives
    private int grindTime;
    private int lastGrindTime = 0;
    public static final int DEFAULT_GRIND_TIME = 60;
    public static final int DRAW_PER_TICK = 10;
    private boolean isGrinding = false;

    private int syncTick = 0;

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

    public boolean getIsGrinding() {
        return this.isGrinding;
    }

    public int getGrinderProgress() {
        if (!this.isGrinding || (this.lastGrindTime <= 0)) {
            return 0;
        }

        return Math.min(100, Math.max(0, Math.round((float) (this.lastGrindTime - Math.max(0, this.grindTime)) * 100.0f / (float) this.lastGrindTime)));
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
        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound comp = new NBTTagCompound();
                comp.setInteger("Slot", i);
                this.inventory[i].writeToNBT(comp);
                list.appendTag(comp);
            }
        }
        compound.setTag("Inventory", list);
        compound.setBoolean("IsGrinding", this.isGrinding);
        compound.setInteger("GrindTime", this.grindTime);
        compound.setInteger("LastGrindTime", this.lastGrindTime);
        compound.setTag("Container", this.container.serializeNBT());
        compound = super.writeToNBT(compound);

        if (pos != null) {
            int dim = 0;
            for (int i : DimensionManager.getIDs())
                if (DimensionManager.getWorld(i).equals(worldObj)) {
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

        if (compound.hasKey("Container"))
            this.container.deserializeNBT((NBTTagCompound)compound.getTag("Container"));
        NBTTagList list = compound.getTagList("Inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound c = list.getCompoundTagAt(i);
            int slot = c.getInteger("Slot");
            if (slot >= 0 && slot < this.inventory.length)
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(c);
        }
        this.isGrinding = compound.getBoolean("IsGrinding");
        this.grindTime = compound.getInteger("GrindTime");
        this.lastGrindTime = compound.getInteger("LastGrindTime");
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
        if (slot == 1) return stack;
        if (inventory[slot] == null) {
            if (!simulate) inventory[slot] = stack.copy();
            return null;
        }
        if (inventory[slot].isItemEqual(stack)) {
            if (inventory[slot].stackSize + stack.stackSize <= 64 && inventory[slot].stackSize + stack.stackSize <= stack.getMaxStackSize()) {
                if (!simulate) inventory[slot].stackSize += stack.stackSize;
                return null;
            }
            tmp = stack.copy();
            tmp.stackSize = stack.getMaxStackSize() - inventory[slot].stackSize - stack.stackSize;
            if (!simulate) inventory[slot].stackSize = stack.getMaxStackSize();
            return tmp;
        }
        if (simulate) return inventory[slot];
        tmp = inventory[slot];
        inventory[slot] = stack;
        if (isGrinding) { // If item are switched while grinding, grinding stops
            isGrinding = false;
            grindTime = 0;
        }
        return tmp;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack split;
        if (inventory[slot] == null) return null;
        if (amount >= inventory[slot].stackSize) {
            split = inventory[slot];
            if (!simulate) {
                inventory[slot] = null;
                if (slot == 0 && this.isGrinding) {
                    this.isGrinding = false;
                    this.grindTime = 0;
                }
            }
            return split;
        }
        if (simulate) {
            (split = inventory[slot].copy()).stackSize = amount;
        } else {
            split = inventory[slot].splitStack(amount);
        }
        return split;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability==CAPABILITY_CONSUMER || capability==CAPABILITY_HOLDER || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
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
        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound comp = new NBTTagCompound();
                comp.setInteger("Slot", i);
                this.inventory[i].writeToNBT(comp);
                list.appendTag(comp);
            }
        }
        compound.setTag("Inventory", list);
        compound.setBoolean("IsGrinding", this.isGrinding);
        compound.setInteger("GrindTime", this.grindTime);
        compound.setInteger("LastGrindTime", this.lastGrindTime);
        compound.setTag("Container", this.container.serializeNBT());

        compound = super.writeToNBT(compound);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Container"))
            this.container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
        NBTTagList list = compound.getTagList("Inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound c = list.getCompoundTagAt(i);
            int slot = c.getInteger("Slot");
            if (slot >= 0 && slot < this.inventory.length)
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(c);
        }
        this.isGrinding = compound.getBoolean("IsGrinding");
        this.grindTime = compound.getInteger("GrindTime");
        this.lastGrindTime = compound.getInteger("LastGrindTime");
    }

//    @SubscribeEvent
//    public void onEntityJoinEvent(EntityJoinWorldEvent event){
//        //NOP
//    }

    @Override
    public void update() {
//        if (this.isGrinding) {
//            if (this.grindTime == 0) {
//                ItemStack s = extractItem(0, 1, false);
//                if (s == null) { // Invalid state that for some reason can arise
//                    // isGrinding = false;
//                }
//                else {
//                    if (this.inventory[1] == null) {
//                        this.inventory[1] = TeslaRegistry.teslaRegistry.getGrinderOutFromIn(s).copy();
//                        this.inventory[1].stackSize = TeslaRegistry.teslaRegistry.getGrinderRecipeFromIn(s).getAmount();
//                    } else {
//                        this.inventory[1].stackSize += TeslaRegistry.teslaRegistry.getGrinderRecipeFromIn(s).getAmount();
//                    }
//                }
//                this.isGrinding = false;
//            }
//            else if (container.getStoredPower() < DRAW_PER_TICK) {
//                this.isGrinding = false;
//                this.grindTime = 0;
//            }
//            else {
//                this.container.takePower(DRAW_PER_TICK, false);
//                --this.grindTime;
//            }
//        } else if (inventory[0] != null && TeslaRegistry.teslaRegistry.hasRecipe(inventory[0]) && (inventory[1] == null ||
//                inventory[1].isItemEqual(TeslaRegistry.teslaRegistry.getGrinderOutFromIn(inventory[0]))) && container.getStoredPower() > 0 &&
//                (inventory[1] == null || inventory[1].stackSize < 64)) {
//            this.isGrinding = true;
//            this.grindTime = this.lastGrindTime = DEFAULT_GRIND_TIME;
//        }
//
//        if (this.syncTick == 10 && !this.worldObj.isRemote) {
//            if (this.pos != null) {
//                int dim = 0;
//                for (int i : DimensionManager.getIDs())
//                    if (DimensionManager.getWorld(i).equals(this.worldObj)) {
//                        dim = i;
//                        break;
//                    }
//                ModSquad.channel.sendToAll(new TileDataSync(this.pos, serializeNBT().toString(), dim));
//            }
//            this.syncTick = 0;
//        } else if (this.syncTick < 10) {
//            ++this.syncTick;
//        }

        if (this.isGrinding) {
            if (this.grindTime <= 0) {
                ItemStack input = this.extractItem(0, 1, true);
                if ((input != null) && !this.worldObj.isRemote) { // we only do the actual smelting on server side
                    ItemStack output = TeslaRegistry.teslaRegistry.getGrinderOutFromIn(input).copy();
                    ItemStack target = this.getStackInSlot(1);
                    boolean fail = false;
                    if (target == null) {
                        target = output;
                    } else if (target.isItemEqual(output)) {
                        target = target.copy();
                        if ((target.stackSize + output.stackSize) <= target.getMaxStackSize()) {
                            target.stackSize += output.stackSize;
                        }
                        else {
                            fail = true;
                        }
                    } else {
                        fail = true;
                    }

                    if (!fail) {
                        this.extractItem(0, 1, false);
                        this.setStackInSlot(1, target);
                    }
                }
                this.isGrinding = false;
            }
            if (this.container.getStoredPower() < DRAW_PER_TICK) {
                this.isGrinding = false;
                this.grindTime = 0;
            } else {
                this.container.takePower(DRAW_PER_TICK, false);
                --this.grindTime;
            }
        } else if (this.container.getStoredPower() >= DRAW_PER_TICK) {
            ItemStack input = this.getStackInSlot(0);
            if ((input != null) && (input.stackSize > 0)) {
                input = input.copy();
                input.stackSize = 1;
                ItemStack output = TeslaRegistry.teslaRegistry.getGrinderOutFromIn(input).copy();
                ItemStack target = this.getStackInSlot(1);
                if ((output != null) && (output.stackSize > 0)
                        && ((target == null) || (target.isItemEqual(output)))
                        && (((target == null) ? 0 : target.stackSize) + output.stackSize <= output.getMaxStackSize())) {
                    this.isGrinding = true;
                    this.grindTime = this.lastGrindTime = DEFAULT_GRIND_TIME;
                }
            }
        }

        if (this.syncTick >= 10 && !this.worldObj.isRemote) {
            if (this.pos != null) {
                int dim = 0;
                for (int i : DimensionManager.getIDs())
                    if (DimensionManager.getWorld(i).equals(this.worldObj)) {
                        dim = i;
                        break;
                    }
                ModSquad.channel.sendToAll(new TileDataSync(this.pos, serializeNBT().toString(), dim));
            }
            this.syncTick = 0;
        } else if (this.syncTick < 10) {
            ++this.syncTick;
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack!=null?stack.copy():null;
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
