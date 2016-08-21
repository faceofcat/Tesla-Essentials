package com.trials.modsquad.block.machine;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.tile.TileCable;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        System.out.println("Update");
        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB bb = new AxisAlignedBB(pos);
        bb.addCoord(0.3, 0.3, 0);
        bb.addCoord(0.6, 0.3, 0);
        bb.addCoord(0.6, 0.6, 0);
        bb.addCoord(0.3, 0.6, 0);
        bb.addCoord(0.3, 0.6, 1);
        bb.addCoord(0.6, 0.3, 1);
        bb.addCoord(0.6, 0.6, 1);
        bb.addCoord(0.3, 0.6, 1);
        return bb;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) { return 0; }

    @Override
    public boolean isVisuallyOpaque() { return false; }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) { return false; }

    @Override
    public int getMetaFromState(IBlockState state) { return 0; }

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
