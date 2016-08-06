package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;

import java.lang.reflect.Field;
import java.util.List;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_PRODUCER;

public class ModArmor extends ItemArmor {

    private static int[] armor = {3,8,6,3};
    public static ItemArmor.ArmorMaterial electricArmor = EnumHelper.addArmorMaterial("electricalArmor", "modsquad:electricalArmor",
            15, armor, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 4F);

    private BaseTeslaContainer container;
    private final Field itemDamage;
    private final Field damageReduce;

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
        container = (BaseTeslaContainer) stack.getCapability(CAPABILITY_HOLDER, EnumFacing.DOWN);

        tooltip.add("Power: " + container.getStoredPower() + "/" + container.getCapacity());
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        ITeslaConsumer c = itemStack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN);

        /*if (player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() == ModItems.electricHelmet) {
            if (h.getStoredPower() == 0) { armor[0] = 0; } else { armor[0] = 3; }
        } else if (player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() == ModItems.electricChestplate) {
            if (h.getStoredPower() == 0) { armor[1] = 0; } else { armor[1] = 8; }
        } else if (player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() == ModItems.electricLeggings) {
            if (h.getStoredPower() == 0) { armor[2] = 0; } else { armor[2] = 6; }
        } else if (player.inventory.armorItemInSlot(0) != null && player.inventory.armorItemInSlot(0).getItem() == ModItems.electricBoots) {
            if (h.getStoredPower() == 0) { armor[3] = 0; } else { armor[3] = 3; }
        }*/

    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (armorType == EntityEquipmentSlot.LEGS) {
            return "modsquad:textures/models/armor/electricalArmor_layer_2";
        }
        return "modsquad:textures/models/armor/electricalArmor_layer_1";
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
        // As stored power increases, dam tends towards the value getMaxDamage()
        int dam = h.getCapacity()>0?Math.round(h.getStoredPower()*(getMaxDamage()-1)/h.getCapacity()):0;
        try{ itemDamage.setInt(stack, getMaxDamage()-dam); }catch(Exception e){}
        ITeslaProducer p = stack.getCapability(CAPABILITY_PRODUCER, EnumFacing.DOWN);
        if(h.getStoredPower()==0)try{ damageReduce.setInt(this, 0); }catch(Exception e){}
    }

    @Override
    public boolean isRepairable() {
        return false;
    }
}
