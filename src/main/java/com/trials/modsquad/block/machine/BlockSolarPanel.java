package com.trials.modsquad.block.machine;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.tile.TileSolarPanel;
import com.trials.net.ChatSync;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;
import static com.trials.modsquad.ModSquad.MODID;

@SuppressWarnings("deprecation")
public class BlockSolarPanel extends Block {
    public BlockSolarPanel(String name, String reg) {
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
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileSolarPanel();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.getTileEntity(pos) == null || playerIn.isSneaking() || worldIn.isRemote) return false;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        long power = tileentity==null?0:tileentity.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN).getStoredPower();
        long cap = tileentity==null?20000:tileentity.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN).getCapacity();
        //playerIn.addChatMessage(new TextComponentString("Power: " + power + "/" + cap));
        if(playerIn instanceof EntityPlayerMP) ChatSync.forMod(MODID).sendPlayerChatMessage((EntityPlayerMP)playerIn, "Power: " + power + "/" + cap, Ref.GUI_CHAT_POWER); // No-spam chat message
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) { // Drop item when block breaks
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        TileEntity t = worldIn.getTileEntity(pos);
        if(!(t instanceof IInventory)) return;
        ItemStack s;
        for(int i = 0; i<((IInventory) t).getSizeInventory(); ++i)
            if((s = ((IInventory) t).getStackInSlot(i))!=null && s.stackSize > 0){
                EntityItem item = new EntityItem(worldIn, rand.nextFloat()*0.8f+0.1f, rand.nextFloat()*.8f+.1f, rand.nextFloat()*.8f+.1f,
                        new ItemStack(s.getItem(), s.stackSize, s.getItemDamage()));
                if(s.hasTagCompound() && s.getTagCompound()!=null) item.getEntityItem().setTagCompound(s.getTagCompound().copy());
                item.motionX = rand.nextGaussian() * .05f;
                item.motionY = rand.nextGaussian() * .05f + .2f;
                item.motionZ = rand.nextGaussian() * .05f;
                worldIn.spawnEntityInWorld(item);
                s.stackSize = 0;
            }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    private static double x = 0.0625;
    private static final AxisAlignedBB bBox = new AxisAlignedBB(0,0,0,16*x,5*x,16*x);
    private static final AxisAlignedBB cBox = new AxisAlignedBB(0,0,0,16*x,5*x,16*x);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return bBox;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return cBox;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
