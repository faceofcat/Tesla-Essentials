package com.trials.modsquad.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.trials.modsquad.ModSquad;
import com.trials.modsquad.Ref;
import com.trials.net.ChatSync;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.trials.modsquad.ModSquad.MODID;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class PoweredPotato extends ItemFood {

    private final Field itemDamage;
    private long drain = 400;
    private boolean electricPotatoBreakChance;
    private int breakChance = 5;

    public PoweredPotato(String unlocalizedName, String registryName, boolean electricPotatoBreakChance, int breakChance) {
        super(4, 0.8F, false);
        setUnlocalizedName(unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(Ref.tabModSquad);
        setMaxStackSize(1);
        setMaxDamage(240);
        Field f = null;
        try {
            f = ItemStack.class.getDeclaredField("itemDamage");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) { }
        this.itemDamage = f;
        this.electricPotatoBreakChance = electricPotatoBreakChance;
        if(breakChance < 50)
            this.breakChance = breakChance;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        BaseTeslaContainer container = (BaseTeslaContainer) stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        tooltip.add(ChatFormatting.BLUE+"Shocking taste!");
        tooltip.add("Power: " + container.getStoredPower() + "/" + container.getCapacity());
        if(electricPotatoBreakChance)
            tooltip.add(ChatFormatting.DARK_RED+"Has a "+breakChance+"% chance to break");
        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Nullable
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        BaseTeslaContainer container = (BaseTeslaContainer) stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (entityLiving instanceof EntityPlayer && container.getStoredPower() > 400) {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            container.takePower(drain, false);
            entityplayer.getFoodStats().setFoodLevel(entityplayer.getFoodStats().getFoodLevel() + 4);
            entityplayer.getFoodStats().setFoodSaturationLevel(entityplayer.getFoodStats().getSaturationLevel() + 0.8F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            if(!worldIn.isRemote && electricPotatoBreakChance && ThreadLocalRandom.current().nextInt(0,100) < breakChance){
                if(entityLiving instanceof EntityPlayerMP) ChatSync.forMod(ModSquad.MODID).sendPlayerChatMessage((EntityPlayerMP) entityLiving, "The Capacitors Overloaded;/n All that remains is a Baked Potato", Ref.ITEM_ID_POTATO);
                return new ItemStack(Items.BAKED_POTATO, 1);
            }

        }
        return stack;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new BaseTeslaContainerProvider(new BaseTeslaContainer(80000, 20, drain){
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
