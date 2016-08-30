package com.trials.modsquad.block.machine;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.ModBlocks;
import com.trials.modsquad.block.tile.TileCable;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemClock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings({"unused", "deprecation"})
public class BasicCable extends Block {
    private long inputRate;
    private long outputRate;

    private Ref.BlockReference type;

    public static final PropertyBool    NORTH   =   PropertyBool.create("north");
    public static final PropertyBool    SOUTH   =   PropertyBool.create("south");
    public static final PropertyBool    EAST    =   PropertyBool.create("east");
    public static final PropertyBool    WEST    =   PropertyBool.create("west");
    public static final PropertyBool    UP      =   PropertyBool.create("up");
    public static final PropertyBool    DOWN    =   PropertyBool.create("down");

    public static final PropertyBool[] CARDINALS=   {NORTH, SOUTH, EAST, WEST, UP, DOWN};

    public static final AxisAlignedBB[] sides   =   new AxisAlignedBB[]{
            new AxisAlignedBB(0.333, 0.333, 0, 0.666, 0.666, 0.666), // North (Negative Z)
            new AxisAlignedBB(0.333, 0.333, 0.333, 0.666, 0.666, 1), // South (Positive Z)
            new AxisAlignedBB(0.333, 0.333, 0.333, 1, 0.666, 0.666), // East (Positive X)
            new AxisAlignedBB(0, 0.333, 0.333, 0.666, 0.666, 0.666), // West (Negative X)
            new AxisAlignedBB(0.333, 0.333, 0.333, 0.666, 1, 0.666), // Up (Positive Y)
            new AxisAlignedBB(0.333, 0, 0.333, 0.666, 0.666, 0.666)  // Down (Negative Y)
    };

    public BasicCable(String unloc, String reg) {
        super(Material.GROUND);
        this.inputRate = unloc.equals(Ref.BlockReference.LEAD_CABLE.getUnlocalizedName())?20:0;
        this.outputRate = inputRate==20?20:0;
        setUnlocalizedName(unloc);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
        //Should give 576 unique combinations
        setDefaultState(blockState.getBaseState().withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false)
                .withProperty(UP, false).withProperty(DOWN, false));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TileCable(inputRate, outputRate); }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem,
                                    EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //clock: Wrench
        if(heldItem != null && heldItem.getItem().getClass() == ItemClock.class) worldIn.destroyBlock(pos, true); //Specifically ItemClock! No other derivatives count
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP, DOWN); }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase user, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, updateModel(world, pos, getDefaultState(), true), user, stack);
        world.scheduleBlockUpdate(pos, this, 0, 1);
    }

    public static IBlockState updateModel(World world, BlockPos pos, IBlockState defaultState, boolean simulate){
        List<EnumFacing> connected = new ArrayList<>();
        int i = -1;
        TileEntity e;
        for(EnumFacing f : EnumFacing.VALUES)
            if((e=world.getTileEntity(pos.offset(f)))!=null && (e.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, f.getOpposite()) ||
                    e.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, f.getOpposite()))) connected.add(f);

        // Ensure that a valid BlockState is provided, even if the block isn't available
        IBlockState base = world.getBlockState(pos)!=Blocks.AIR.getDefaultState()?world.getBlockState(pos):defaultState;

        IBlockState state = base.withProperty(NORTH, connected.contains(EnumFacing.NORTH))
                .withProperty(SOUTH, connected.contains(EnumFacing.SOUTH))
                .withProperty(EAST, connected.contains(EnumFacing.EAST))
                .withProperty(WEST, connected.contains(EnumFacing.WEST))
                .withProperty(UP, connected.contains(EnumFacing.UP))
                .withProperty(DOWN, connected.contains(EnumFacing.DOWN));

        if(!simulate) world.setBlockState(pos, state);

        return state;
    }

    /*@Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return super.getCollisionBoundingBox(blockState, worldIn, pos);
    }*/

    private void prnt(String arg) {
        System.out.println(arg);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB base = new AxisAlignedBB(0.333, 0.333, 0.333, 0.666, 0.666, 0.666);
        for(int i = 0; i<CARDINALS.length; ++i) if(state.getValue(CARDINALS[i])) base = base.union(sides[i]);
        return base;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) { return false; }

    @Override
    public int getMetaFromState(IBlockState state) { return 0; }

    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }
    @Override public boolean isCollidable() { return true; }
    @Override public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) { return false; }
    @Override public boolean isNormalCube(IBlockState state) { return false; }
    @Override public boolean isVisuallyOpaque() { return false; }
    @Override public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) { return false; }
    @Override public boolean isBlockNormalCube(IBlockState state) { return false; }
    @Override public boolean isFullyOpaque(IBlockState state) { return false; }
    @Override public boolean isFullBlock(IBlockState state) { return false; }
    @Override public boolean isTranslucent(IBlockState state) { return false; }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false).withProperty(UP, false)
                .withProperty(DOWN, false);
    }

    public enum CableType{
        IRON("iron");

        String name;
        CableType(String name){
            this.name = name;
        }
    }

}
