package com.trials.modsquad.block.tile;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.States;
import com.trials.modsquad.block.machine.BlockElectricFurnace;
import com.trials.net.TileDataSync;
import com.trials.net.Updatable;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import javax.annotation.Nullable;
import static com.trials.modsquad.block.machine.BlockElectricFurnace.STATE;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class TileElectricFurnace extends TileEntity implements IItemHandlerModifiable, ITickable, Updatable {
    /**
     * Inventory slot 0: Input
     * Inventory slot 1: Output
     */
    private ItemStack[] inventory;
    private BaseTeslaContainer container;
    private boolean isSmelting = false;
    private int lastWorkTime = 0;
    private int workTime = 0;
    private int syncTick = 500;

    static final int DRAW_PER_TICK = 10;
    static final int DEFAULT_SMELT_TIME = 60;

    private IItemHandlerModifiable extractor;
    private IItemHandlerModifiable inserter;

    public TileElectricFurnace() {
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
                return inventory[slot + 1];
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return stack;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return TileElectricFurnace.this.extractItem(slot + 1, amount, simulate);
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
                return inventory[slot];
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

    public boolean getIsSmelting() {
        return this.isSmelting;
    }

    public int getSmeltProgress() {
        if (!this.isSmelting || (this.lastWorkTime <= 0)) {
            return 0;
        }

        return Math.min(100, Math.max(0, Math.round((float)(this.lastWorkTime - Math.max(0, this.workTime)) * 100.0f / (float)this.lastWorkTime)));
    }

    private int getSmeltTime(ItemStack stack) {
        return DEFAULT_SMELT_TIME;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound t = new NBTTagCompound();
        t = writeToNBT(t);
        return new SPacketUpdateTileEntity(pos, 0, t);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null) {
                NBTTagCompound comp = new NBTTagCompound();
                comp.setInteger("Slot", i);
                inventory[i].writeToNBT(comp);
                list.appendTag(comp);
            }
        }
        compound.setTag("Inventory", list);
        compound.setBoolean("IsSmelting", this.isSmelting);
        compound.setInteger("WorkTime", this.workTime);
        compound.setInteger("LastWorkTime", this.lastWorkTime);
        compound.setTag("Container", this.container.serializeNBT());

        if (pos != null) {
            int dim = 0;
            for (int i : DimensionManager.getIDs()) {
                if (DimensionManager.getWorld(i).equals(this.worldObj)) {
                    dim = i;
                    break;
                }
            }
            ModSquad.channel.sendToAll(new TileDataSync(pos, compound.toString(), dim));
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("Container")) {
            container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
        }
        NBTTagList list = compound.getTagList("Inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound c = list.getCompoundTagAt(i);
            int slot = c.getInteger("Slot");
            if ((slot >= 0) && (slot < this.inventory.length)) {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(c);
            }
        }
        this.isSmelting = compound.getBoolean("IsSmelting");
        this.workTime = compound.getInteger("WorkTime");
        this.lastWorkTime = compound.getInteger("LastWorkTime");
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.readFromNBT(compound);
//        super.readFromNBT(compound);
//        if (compound.hasKey("Container")) container.deserializeNBT((NBTTagCompound) compound.getTag("Container"));
//        NBTTagList list = compound.getTagList("Inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
//        for (int i = 0; i < list.tagCount(); ++i) {
//            NBTTagCompound c = list.getCompoundTagAt(i);
//            int slot = c.getInteger("Slot");
//            if (slot >= 0 && slot < inventory.length) inventory[slot] = ItemStack.loadItemStackFromNBT(c);
//        }
//        isSmelting = compound.getBoolean("IsGrinding");
//        workTime = compound.getInteger("GrindTime");
    }

//    @SubscribeEvent
//    public void onEntityJoinEvent(EntityJoinWorldEvent event){
//        //NOP
//    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void update() {
        // Locks the current smelting process until more power is received
//        if (container.getStoredPower() < DRAW_PER_TICK) {
//            if (this.isSmelting) {
//                worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(STATE, States.ActiveState.INACTIVE));
//                this.isSmelting = false;
//            }
//            // return;
//        }

        // ModSquad.logger.info("smelting: " + this.isSmelting + ", tick: " + this.workTime);
        if (this.isSmelting) {
            if (this.worldObj.getBlockState(this.pos).getProperties().get(STATE).equals(States.ActiveState.INACTIVE)) {
                this.worldObj.setBlockState(this.pos, this.worldObj.getBlockState(this.pos).withProperty(STATE, States.ActiveState.ACTIVE));
            }

            if (this.workTime <= 0) {
                ItemStack s = extractItem(0, 1, false);
                if (s == null) { // Invalid state that for some reason can arise
//                    this.isSmelting = false;
//                    workTime = 0;
                }
                else if (!this.worldObj.isRemote) { // we only do the actual smelting on server side
                    ItemStack output = this.getStackInSlot(1);
                    if (output == null) {
                        output = FurnaceRecipes.instance().getSmeltingResult(s).copy();
                    } else {
                        output.stackSize += FurnaceRecipes.instance().getSmeltingResult(s).stackSize;
                    }

                    // make sure we are not dealing with a copy
                    this.setStackInSlot(1, output);
                }
                this.isSmelting = false;
            }
            if (this.container.getStoredPower() < DRAW_PER_TICK) {
                // ModSquad.logger.info("ran out of power!");
                this.isSmelting = false;
                this.workTime = 0;
            } else {
                this.container.takePower(DRAW_PER_TICK, false);
                --this.workTime;
            }
        } else {
            if (this.worldObj.getBlockState(this.pos).getProperties().get(STATE).equals(States.ActiveState.ACTIVE)) {
                this.worldObj.setBlockState(this.pos, this.worldObj.getBlockState(this.pos).withProperty(STATE, States.ActiveState.INACTIVE));
            }

            if (this.container.getStoredPower() >= DRAW_PER_TICK) {
                ItemStack stack = this.getStackInSlot(0); // inventory[0] != null ? FurnaceRecipes.instance().getSmeltingResult(inventory[0]) : null;
                if ((stack != null) && (stack.stackSize > 0)) {
                    stack = stack.copy();
                    stack.stackSize = 1;
                    ItemStack smeltedStack = FurnaceRecipes.instance().getSmeltingResult(stack);
                    ItemStack output = this.getStackInSlot(1);
                    if ((smeltedStack != null) && (smeltedStack.stackSize > 0)
                            && ((output == null) || (output.isItemEqual(smeltedStack)))
                            && (((output == null) ? 0 : output.stackSize) + smeltedStack.stackSize <= smeltedStack.getMaxStackSize())) {
                        this.isSmelting = true;
                        this.workTime = this.lastWorkTime = this.getSmeltTime(smeltedStack);
                    }
                }
            }
//            int size;
//            if (itemstack != null && (inventory[1] == null || inventory[1].isItemEqual(itemstack)) && (size = (inventory[1] != null ? inventory[1].stackSize : 0) + itemstack.stackSize) <= 64 &&
//                    size <= itemstack.getMaxStackSize()) {
//                this.isSmelting = true;
//                workTime = 60;
//            }
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        // if we don't do this the entity gets reset on every state change and we lose isSmelting and workTick
        return (false == (newState.getBlock() instanceof BlockElectricFurnace));
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory[slot] = stack != null ? stack.copy() : null;
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
        if (simulate)
            return inventory[slot];
        tmp = inventory[slot];
        inventory[slot] = stack;
        if (isSmelting) {
            isSmelting = false;
            workTime = 0;
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
                if (slot == 0 && isSmelting) {
                    isSmelting = false;
                    workTime = 0;
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
        return capability == CAPABILITY_CONSUMER || capability == CAPABILITY_HOLDER || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CAPABILITY_CONSUMER || capability == CAPABILITY_HOLDER) return (T) container;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) return (T) extractor;
            else return (T) inserter;
        }
        return super.getCapability(capability, facing);
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
