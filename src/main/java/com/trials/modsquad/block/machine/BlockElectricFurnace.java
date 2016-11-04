package com.trials.modsquad.block.machine;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.Ref;
import com.trials.modsquad.block.States;
import com.trials.modsquad.block.tile.TileElectricFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nullable;
import static com.trials.modsquad.Ref.GUI_ID_FURNACE;

@SuppressWarnings("deprecation")
public class BlockElectricFurnace extends Block {
    public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<States.ActiveState> STATE = PropertyEnum.create("state", States.ActiveState.class);

    public BlockElectricFurnace(String s, String s1) {
        super(Material.IRON);
        setUnlocalizedName(s);
        setRegistryName(s1);
        setCreativeTab(Ref.tabModSquad);
        setHarvestLevel("pickaxe", 1);
        setResistance(30F);
        setHardness(5F);
        setDefaultState(blockState.getBaseState().withProperty(PROPERTYFACING, EnumFacing.NORTH).withProperty(STATE, States.ActiveState.INACTIVE));
    }

    @Override
    protected BlockStateContainer createBlockState(){ return new BlockStateContainer(this, PROPERTYFACING, STATE); }

    /*
        Big-Endian
        0 00
        | ||
        | Facing
        ActiveState
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int value = 0;
//        for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
//            if (state.getProperties().get(PROPERTYFACING).equals(facing)) {
//                value = facing.ordinal();
//                break;
//            }
        value = state.getValue(PROPERTYFACING).getIndex();
        value = (value << 1) + (state.getProperties().get(STATE).equals(States.ActiveState.INACTIVE) ? 0 : 1);
        return value;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int active = meta % 2;
        EnumFacing facing = EnumFacing.getFront(meta >> 1);
        if (facing.getAxis() == EnumFacing.Axis.Y) { facing = EnumFacing.NORTH; }
        try {
            return getDefaultState()
                    .withProperty(PROPERTYFACING, facing)
                    .withProperty(STATE, (active == 1) ? States.ActiveState.ACTIVE : States.ActiveState.INACTIVE);
        } catch (Exception e) {
            return getDefaultState();
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase user, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, user,stack);
        world.setBlockState(pos, state.withProperty(PROPERTYFACING, user.getHorizontalFacing().rotateAround(EnumFacing.Axis.Y)).withProperty(STATE, States.ActiveState.INACTIVE));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.getTileEntity(pos) == null || playerIn.isSneaking()) return false;
        playerIn.openGui(ModSquad.instance, GUI_ID_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TileElectricFurnace(); }

    @Override
    public boolean hasTileEntity(IBlockState state) { return true; }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) { // Drop item when block breaks
        TileEntity t = worldIn.getTileEntity(pos);
        if (t instanceof IItemHandler) {
            IItemHandler h = (IItemHandler) t;
            for (int i = 0; i < h.getSlots(); ++i) {
                if (h.getStackInSlot(i) != null && h.getStackInSlot(i).stackSize > 0)
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override //Teehee "block" rendering :P
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) { return false; }
}
