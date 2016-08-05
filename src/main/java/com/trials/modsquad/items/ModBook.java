package com.trials.modsquad.items;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.Ref;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static com.trials.modsquad.Ref.GUI_ID_BOOK;

public class ModBook extends ItemBook {

    public ModBook(String name, String reg) {
        setUnlocalizedName(name);
        setRegistryName(reg);
        setMaxStackSize(1);
        setCreativeTab(Ref.tabModSquad);

    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote) {
            playerIn.openGui(ModSquad.instance, GUI_ID_BOOK, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

}
