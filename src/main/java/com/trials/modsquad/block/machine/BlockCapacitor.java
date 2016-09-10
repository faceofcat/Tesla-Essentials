package com.trials.modsquad.block.machine;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.tile.TileCapacitor;
import com.trials.net.ChatSync;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import static com.trials.modsquad.ModSquad.MODID;

@SuppressWarnings("deprecation")
public class BlockCapacitor extends Block {

    public BlockCapacitor (String name, String reg) {
        super(Material.IRON);
        setUnlocalizedName(name);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
        setHarvestLevel("pickaxe", 1);
        setResistance(30F);
        setHardness(5F);
        setDefaultState(blockState.getBaseState().withProperty(PROPERTYFACING, EnumFacing.NORTH));
    }

    public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, PROPERTYFACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PROPERTYFACING).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(PROPERTYFACING, meta>1?EnumFacing.values()[meta] : EnumFacing.NORTH);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase user, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, user,stack);
        world.setBlockState(pos, state.withProperty(PROPERTYFACING, user.getHorizontalFacing()));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.getTileEntity(pos) == null || playerIn.isSneaking() || worldIn.isRemote) return false;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if(tileentity==null) return false;
        long power = tileentity.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN).getStoredPower();
        long cap = tileentity.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN).getCapacity();
        if(playerIn instanceof EntityPlayerMP) //playerIn.addChatMessage(new TextComponentString("Power: " + power + "/" + cap));
            ChatSync.forMod(MODID).sendPlayerChatMessage((EntityPlayerMP) playerIn, "Power: " + power + "/" + cap, Ref.GUI_CHAT_POWER);
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCapacitor();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) { return true; }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }
}
