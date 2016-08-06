package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class PoweredPotato extends ItemFood {

    private BaseTeslaContainer container;

    public PoweredPotato(String unlocalizedName, String registryName) {
        super(4, 0.8F, false);
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(Ref.tabModSquad);
    }

    @Nullable
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer && container.getStoredPower() > 49) {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            container.takePower(50, false);
            this.onFoodEaten(stack, worldIn, entityplayer);
        }
        return stack;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new BaseTeslaContainerProvider(new BaseTeslaContainer(){
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
        stack.setItemDamage((int) h.getStoredPower() / stack.getMaxDamage());
    }

    @Override
    public boolean isRepairable() {
        return false;
    }
}
