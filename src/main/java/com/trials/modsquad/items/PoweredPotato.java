package com.trials.modsquad.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.List;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class PoweredPotato extends ItemFood {

    private final Field itemDamage;

    public PoweredPotato(String unlocalizedName, String registryName) {
        super(4, 0.8F, false);
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(Ref.tabModSquad);
        setMaxStackSize(1);
        setMaxDamage(40);
        Field f = null;
        try {
            f = ItemStack.class.getDeclaredField("itemDamage");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) { }
        this.itemDamage = f;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        BaseTeslaContainer container = (BaseTeslaContainer) stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        tooltip.add(ChatFormatting.BLUE+"Shocking taste!");
        tooltip.add("Power: " + container.getStoredPower() + "/" + container.getCapacity());
        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Nullable
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        BaseTeslaContainer container = (BaseTeslaContainer) stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (entityLiving instanceof EntityPlayer && container.getStoredPower() > 49) {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            container.takePower(50, false);
            entityplayer.getFoodStats().setFoodLevel(entityplayer.getFoodStats().getFoodLevel() + 4);
            entityplayer.getFoodStats().setFoodSaturationLevel(entityplayer.getFoodStats().getSaturationLevel() + 0.8F);
            this.onFoodEaten(stack, worldIn, entityplayer);
        }
        return stack;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new BaseTeslaContainerProvider(new BaseTeslaContainer(2000, 20, 20){
            @Override
            public long givePower(long Tesla, boolean simulated) {
                setDamage(stack, 0);
                return super.givePower(Tesla, simulated);
            }

            @Override
            public long takePower(long Tesla, boolean simulated) {
                setDamage(stack, 0);
                return super.takePower(Tesla, simulated);
            }
        });
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ITeslaHolder h=stack.getCapability(CAPABILITY_HOLDER, EnumFacing.DOWN);
        // As stored power increases, dam tends towards the value getMaxDamage()
        int dam = h.getCapacity()>0?Math.round(h.getStoredPower()*(getMaxDamage()-1)/h.getCapacity()):1;
        try{ itemDamage.setInt(stack, getMaxDamage()-dam); }catch(Exception e){}
    }

    @Override
    public boolean isRepairable() {
        return false;
    }
}
