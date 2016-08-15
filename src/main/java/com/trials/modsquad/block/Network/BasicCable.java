package com.trials.modsquad.block.Network;

import com.trials.modsquad.Ref;
import com.trials.modsquad.block.TileEntities.TileCable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemClock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("unused")
public class BasicCable extends Block {
    private long inputRate;
    private long outputRate;

    private Ref.BlockReference type;

    // NOTE: Max amount of states is exceeded if all possible states * bit length per state > 31 (slightly more complicated but yeah...)
    public static final PropertyEnum<Rotation> rX = PropertyEnum.create("rx",         Rotation.class);
    public static final PropertyEnum<Rotation> rY = PropertyEnum.create("ry",         Rotation.class);
    public static final PropertyEnum<Rotation> rZ = PropertyEnum.create("rz",         Rotation.class);
    public static final PropertyEnum<Type>     Tx = PropertyEnum.create("cabletype",  Type.class);

    public BasicCable(String unloc, String reg) {
        super(Material.GROUND);
        this.inputRate = unloc.equals(Ref.BlockReference.LEAD_CABLE.getUnlocalizedName())?20:0;
        this.outputRate = inputRate==20?20:0;
        setUnlocalizedName(unloc);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
        //Should give 576 unique combinations
        setDefaultState(blockState.getBaseState().withProperty(rX, Rotation.r0).withProperty(rY, Rotation.r0).withProperty(rZ, Rotation.r0).withProperty(Tx, Type.c0));
        assert blockState.getValidStates().size()==576 : "Not all BlockStates have been loaded :("; // "BlockStates" because Intellij
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCable(inputRate, outputRate); //MEGADERP
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //clock: Wrench
        if(heldItem != null && heldItem.getItem().getClass() == ItemClock.class) { //Specifically ItemClock! No other derivatives count
            EntityItem i = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this, 1));
            worldIn.spawnEntityInWorld(i);
            this.breakBlock(worldIn,pos,state);
        }
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, rX, rY, rZ, Tx);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase user, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, user,stack);
        world.setBlockState(pos, state.withProperty(rX, Rotation.r0).withProperty(rY, Rotation.r0).withProperty(rZ, Rotation.r0).withProperty(Tx, Type.c0));
    }

    public enum CableType{
        IRON("iron");

        String name;
        CableType(String name){
            this.name = name;
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        System.out.println("Update");
        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(Tx, Type.c0).withProperty(rX, Rotation.r0).withProperty(rY, Rotation.r0).withProperty(rZ, Rotation.r0);
    }

    public enum Rotation implements IStringSerializable {
        r0("0"), r1("1"), r2("2"), r3("3");
        String name;
        Rotation(String name){ this.name = name; }
        public String getName(){ return name; }
        public static Rotation fromValue(int val){
            for(Rotation r : Rotation.values()) if(r.getName().equals(val+"")) return r;
            return r0;
        }
        public static Rotation fromPosition(int position){ return Rotation.values()[position]; }
    }

    public enum Type implements IStringSerializable{
        c0("c0"), c1("c1"), c2_0("c2_0"), c2_1("c2_1"), c3_0("c3_0"), c3_1("c3_1"), c4_0("c4_0"), c4_1("c4_1"), c5("c5"), c6("c6");
        String name;
        Type(String name){ this.name = name; }
        public String getName(){ return name; }
        public static Type fromValue(String val){
            for(Type r : Type.values()) if(r.getName().equals(val)) return r;
            assert false : "Invalid value!";
            return c0;
        }
        public static Type fromData(int connections, boolean straight){
            if(connections<2 || connections>4) return fromValue("c"+connections);
            return fromValue("c"+connections+"_"+(straight?"0":"1"));
        }
        public static Type fromPosition(int position){ return Type.values()[position]; }
    }

}
