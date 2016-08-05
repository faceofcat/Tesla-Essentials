package com.trials.modsquad.block.machines;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.TileEntities.TileFurnaceGenerator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import java.util.concurrent.ThreadLocalRandom;
import static com.trials.modsquad.Ref.GUI_ID_FURNACE_GEN;

public class BlockFurnaceGenerator extends BlockContainer {

    public BlockFurnaceGenerator(String s, String s1){
        super(Material.IRON);
        setUnlocalizedName(s);
        setRegistryName(s1);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFurnaceGenerator();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.getTileEntity(pos) == null || playerIn.isSneaking()) return false;
        playerIn.openGui(ModSquad.instance, GUI_ID_FURNACE_GEN, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
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
}
