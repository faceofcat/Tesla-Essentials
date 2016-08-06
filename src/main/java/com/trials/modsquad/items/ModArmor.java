package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainerProvider;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

public class ModArmor extends ItemArmor {

    private static int[] armor = {3,8,6,3};
    public static ItemArmor.ArmorMaterial electricArmor = EnumHelper.addArmorMaterial("electricalArmor", "modsquad:electricalArmor", 15, armor, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 4F);

    private BaseTeslaContainer container;

    public ModArmor(String name, String reg, ArmorMaterial material, int var1, EntityEquipmentSlot slot) {
        super(material, var1, slot);
        setUnlocalizedName(name);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        container = (BaseTeslaContainer) stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);

        tooltip.add("Power: " + container.getStoredPower() + "/" + container.getCapacity());
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        ITeslaHolder h = stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);

        int power = (int) h.getStoredPower();
        int dam = Math.round(power / h.getCapacity());
        setDamage(stack, dam);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        ITeslaConsumer c = itemStack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN);
        ITeslaHolder h = itemStack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);

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
        return new BaseTeslaContainerProvider(new BaseTeslaContainer());
    }

}
