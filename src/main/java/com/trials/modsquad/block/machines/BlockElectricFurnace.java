package com.trials.modsquad.block.machines;

import com.google.common.base.Predicate;
import com.trials.modsquad.ModSquad;
import com.trials.modsquad.Ref;
import com.trials.modsquad.block.TileEntities.TileElectricFurnace;
import com.trials.modsquad.block.TileEntities.TileFurnaceGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static com.trials.modsquad.Ref.GUI_ID_FURNACE;

public class BlockElectricFurnace extends Block {


    public BlockElectricFurnace(String s, String s1) {
        super(Material.IRON);
        setUnlocalizedName(s);
        setRegistryName(s1);
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
        return getDefaultState().withProperty(PROPERTYFACING, meta>1?EnumFacing.values()[meta]:EnumFacing.NORTH);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase user, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, user,stack);
        world.setBlockState(pos, state.withProperty(PROPERTYFACING, user.getHorizontalFacing()));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.getTileEntity(pos) == null || playerIn.isSneaking()) return false;
        playerIn.openGui(ModSquad.instance, GUI_ID_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileElectricFurnace();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        System.out.println("Update Tick");

        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) { // Drop items when block breaks
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
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }
}
