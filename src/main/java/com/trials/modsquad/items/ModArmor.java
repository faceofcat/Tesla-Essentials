package com.trials.modsquad.items;

import com.trials.modsquad.Ref;
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

import java.util.List;

import static com.trials.modsquad.Ref.deadArmor;
import static com.trials.modsquad.Ref.electricArmor;

public class ModArmor extends ItemArmor {

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
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new BaseTeslaContainerProvider(new BaseTeslaContainer());
    }

    @Override
    public ArmorMaterial getArmorMaterial() {
        if (container.getStoredPower() == 0) {
            return deadArmor;
        } else {
            return electricArmor;
        }
    }
}
