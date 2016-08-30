package com.trials.modsquad.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.lang.reflect.Field;
import java.util.List;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class ModArmor extends ItemArmor {

    private final Field itemDamage;
    private final Field damageReduce;
    public static final int powerDrawPerTickOnFlight = 1;
    public static final int damageMultiplier = 4;
    private int ticks = 0;

    public ModArmor(String name, String reg, ArmorMaterial material, int var1, EntityEquipmentSlot slot) {
        super(material, var1, slot);
        setUnlocalizedName(name);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
        Field f = null;
        try {
            f = ItemStack.class.getDeclaredField("itemDamage");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) { }
        this.itemDamage = f;
        try{

            f = ItemArmor.class.getDeclaredField("damageReduceAmount");
            f.setAccessible(true);
        } catch (Exception e){ }
        this.damageReduce = f;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        setDamage(stack, 0);
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        BaseTeslaContainer container = (BaseTeslaContainer) stack.getCapability(CAPABILITY_HOLDER, EnumFacing.DOWN);
        if(container.getCapacity()!=20000)
            try{
                Field f = BaseTeslaContainer.class.getDeclaredField("capacity");
                f.setAccessible(true);
                f.setLong(container, 20000);
            }catch(Exception e){}
        if (stack.getItem() == ModItems.jetChestplate) {
            tooltip.add(ChatFormatting.BLUE + "Requires power and all");
            tooltip.add(ChatFormatting.BLUE + "other electric armor!");
        }
        tooltip.add("Power: " + container.getStoredPower() + "/" + container.getCapacity());
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        //noinspection ConstantConditions

        if (player.inventory.armorInventory[3] !=null && player.inventory.armorInventory[3].getItem() == ModItems.electricHelmet
                && player.inventory.armorInventory[2] !=null && player.inventory.armorInventory[2].getItem() == ModItems.jetChestplate
                && player.inventory.armorInventory[1] !=null && player.inventory.armorInventory[1].getItem() == ModItems.electricLeggings
                && player.inventory.armorInventory[0] !=null && player.inventory.armorInventory[0].getItem() == ModItems.electricBoots
                && player.inventory.armorInventory[2].getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN).getStoredPower() > 0
                && player.inventory.armorInventory[1].getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN).getStoredPower() > 0
                && player.inventory.armorInventory[0].getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN).getStoredPower() > 0
                && player.inventory.armorInventory[3].getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN).getStoredPower() > 0
                || player.isCreative()) {
            player.capabilities.allowFlying = true;
            if(player.capabilities.isFlying && !player.isCreative()) {
                if (ticks == 80) {
                    player.inventory.armorInventory[2].getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, EnumFacing.DOWN).takePower(powerDrawPerTickOnFlight, false);
                    ticks = 0;
                } else {
                    ticks++;
                }
            }
        } else {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (slot == EntityEquipmentSlot.LEGS) {
            return "modsquad:textures/models/armor/electricArmor_layer_2.png";
        } else if (stack.getItem() == ModItems.jetChestplate) {
            return "modsquad:textures/models/armor/electricJetArmor_layer_1.png";
        } else if (stack.getItem() != ModItems.jetChestplate){
            return "modsquad:textures/models/armor/electricArmor_layer_1.png";
        }
        return null;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new BaseTeslaContainerProvider(new BaseTeslaContainer(20000, 40, 40){
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
        ITeslaProducer p = stack.getCapability(CAPABILITY_PRODUCER, EnumFacing.DOWN);
        if(damage!=0) p.takePower(damageMultiplier*damage, false);
        if(h.getStoredPower()==0)try{ damageReduce.setInt(this, 0); }catch(Exception e){}
        // As stored power increases, dam tends towards the value getMaxDamage()
        int dam = h.getCapacity()>0?Math.round(h.getStoredPower()*(getMaxDamage()-1)/h.getCapacity()):0;
        try{ itemDamage.setInt(stack, getMaxDamage()-dam); }catch(Exception e){}
    }

    @Override
    public boolean isRepairable() {
        return false;
    }
}
