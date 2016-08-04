package com.trials.modsquad.block.TileEntities;

import mcp.MethodsReturnNonnullByDefault;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.MathHelper;


@SuppressWarnings("unused")
public class TileFurnace extends TileEntityFurnace implements ITeslaConsumer, ITeslaHolder {
    private BaseTeslaContainer container;
    /** The ItemStacks that hold the items currently being used in the furnace */
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    /** The number of ticks that the furnace will keep burning */
    private int furnaceBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    @SuppressWarnings("unused")
    public TileFurnace(){
        container = new BaseTeslaContainer();
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    @Override
    public void update()
    {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning())
        {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.isBurning() || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null)
            {
                if (!this.isBurning() && this.canSmelt())
                {
                    this.furnaceBurnTime = TileFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning())
                    {
                        flag1 = true;
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.furnaceItemStacks[0]);
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0)
            {
                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning())
            {
                flag1 = true;
                BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    @Override
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);

            if (this.furnaceItemStacks[2] == null)
            {
                this.furnaceItemStacks[2] = itemstack.copy();
            }
            else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem())
            {
                this.furnaceItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.furnaceItemStacks[0].stackSize;

            if (this.furnaceItemStacks[0].stackSize <= 0)
            {
                this.furnaceItemStacks[0] = null;
            }
        }
    }

    private boolean canSmelt()
    {
        if (this.furnaceItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[2] == null) return true;
            if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
            int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    public int getItemBurnTime()
    {
            if(container.getStoredPower() > 0) {
                container.takePower(10, false);
                return 1;
            }
        return 0;
    }

    public static boolean isItemFuel(ItemStack stack)
    {
        return true;
    }

    @MethodsReturnNonnullByDefault
    @Override
    public String getGuiID()
    {
        return "minecraft:furnace";
    }

    @MethodsReturnNonnullByDefault
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerFurnace(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        for (int i = 0; i < this.furnaceItemStacks.length; ++i)
        {
            this.furnaceItemStacks[i] = null;
        }
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
}
