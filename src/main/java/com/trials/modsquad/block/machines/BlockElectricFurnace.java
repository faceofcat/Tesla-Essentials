package com.trials.modsquad.block.machines;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.trials.modsquad.ModSquad;
import com.trials.modsquad.Ref;
import com.trials.modsquad.block.TileEntities.TileElectricFurnace;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

import static com.trials.modsquad.Ref.GUI_ID_FURNACE;

public class BlockElectricFurnace extends BlockGrinder {

    public static final PropertyEnum ACTIVITY = PropertyEnum.create("active", ActiveState.class);

    public BlockElectricFurnace(String s, String s1) {
        super(s, s1);
        setCreativeTab(Ref.tabModSquad);
        setDefaultState(blockState.getBaseState().withProperty(ACTIVITY, ActiveState.INACTIVE));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVITY);
    }

    @Override
    public int getMetaFromState(IBlockState state) { // Blynd3 IS THE STUPIDEST PERSON IN THE YOONEEVERSE
        return ((ActiveState) state.getValue(ACTIVITY)).getID();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.getTileEntity(pos) == null || playerIn.isSneaking()) return false;
        playerIn.openGui(ModSquad.instance, GUI_ID_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createTileEntity(World worldIn, IBlockState state) {
        return new TileElectricFurnace();
    }
}
