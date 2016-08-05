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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import static com.trials.modsquad.Ref.GUI_ID_FURNACE;

public class BlockElectricFurnace extends BlockGrinder {

    public static final PropertyEnum ACTIVE = PropertyEnum.create("active", ActiveState.class);

    public BlockElectricFurnace(String s, String s1) {
        super(s, s1);
        setCreativeTab(Ref.tabModSquad);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state) { // Blynd3 IS THE STUPIDEST PERSON IN THE YOONEEVERSE
        return ((ActiveState) state.getValue(ACTIVE)).getID();
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
