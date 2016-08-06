package com.trials.modsquad.block.Network;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.TileEntities.TileCable;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemClock;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import javax.annotation.Nullable;

import java.lang.reflect.Field;

import static com.trials.modsquad.Ref.BlockReference.LEAD_CABLE;


@SuppressWarnings("unused")
public class BasicCable extends Block {
    private long inputRate;
    private long outputRate;

    private Ref.BlockReference type;

    public BasicCable(String unloc, String reg) {
        super(Material.ANVIL);
        this.inputRate = unloc.equals(Ref.BlockReference.LEAD_CABLE.getUnlocalizedName())?20:0;
        this.outputRate = inputRate==20?20:0;
        setUnlocalizedName(unloc);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCable(inputRate, outputRate); //MEGADERP
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //clock: Wrench
        if(heldItem != null && heldItem.getItem().getClass() == ItemClock.class) { //Specifically ItemClock! No other derivatives count
            this.breakBlock(worldIn,pos,state);
        }
        return false;
    }
}
