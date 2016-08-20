package com.trials.modsquad.item;

import com.google.common.collect.Sets;
import com.trials.modsquad.Ref;
import com.trials.modsquad.block.ModBlocks;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class TerraSmasher extends ItemTool {
    public static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE,
            Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE,
            Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON,
            Blocks.STONE_PRESSURE_PLATE, Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND,
            Blocks.GRASS_PATH, ModBlocks.blockCopper, ModBlocks.blockTin, ModBlocks.blockLead, ModBlocks.capacitor, ModBlocks.charger, ModBlocks.electricFurnace, ModBlocks.furnaceGen,
            ModBlocks.grinder, ModBlocks.leadCable, ModBlocks.oreCopper, ModBlocks.oreTin, ModBlocks.oreLead);
    public static final Item.ToolMaterial TERRA_SMASHER = EnumHelper.addToolMaterial("TERRA_SMASHER", 3, 20000, 15.0F, 3.0F, 25);
    public static final long drain = 10;
    private final Field itemDamage;

    public TerraSmasher(String name, String reg) {
        super(1.0F, -2.8F, TERRA_SMASHER, EFFECTIVE_ON);
        setUnlocalizedName(name);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
        setMaxStackSize(1);
        setMaxDamage(2000);
        Field f = null;
        try {
            f = ItemStack.class.getDeclaredField("itemDamage");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) { }
        Field k;
        try{
            k = ItemTool.class.getDeclaredField("toolClass");
            k.setAccessible(true);
            k.set(this, "pickaxe");
        }catch(Exception e){}
        this.itemDamage = f;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        BaseTeslaContainer container = (BaseTeslaContainer) itemstack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (container.getStoredPower() >= drain) {
            return false;
        } else {
            player.addChatMessage(new TextComponentString("Error! Out of power!"));
            return true;
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        BaseTeslaContainer container = (BaseTeslaContainer) stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        container.takePower(drain, false);
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
    }
    @Override
    public void addInformation (ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {

        super.addInformation(stack, playerIn, tooltip, advanced);
        final BaseTeslaContainer container = (BaseTeslaContainer) stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        stack.setItemDamage(((int) (container.getStoredPower()/stack.getMaxDamage())));
        tooltip.add("Power: " + container.getStoredPower() + "/" + container.getCapacity());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new BaseTeslaContainerProvider(new BaseTeslaContainer(20000, 20, 20){
            @Override
            public long givePower(long Tesla, boolean simulated) {
                setDamage(stack, 0);
                return super.givePower(Tesla, simulated);
            }

            @Override
            public long takePower(long Tesla, boolean simulated) {
                setDamage(stack, 0);
                return super.takePower(Tesla, simulated);
            }
        });
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ITeslaHolder h=stack.getCapability(CAPABILITY_HOLDER, EnumFacing.DOWN);
        // As stored power increases, dam tends towards the value getMaxDamage()
        int dam = h.getCapacity()>0?Math.round(h.getStoredPower()*(getMaxDamage()-1)/h.getCapacity()):0;
        try{ itemDamage.setInt(stack, getMaxDamage()-dam); }catch(Exception e){}
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

}
