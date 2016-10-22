package com.trials.modsquad.item;

import com.trials.modsquad.Ref;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemGameInfo extends Item {

    public ItemGameInfo(String name, String reg) {
        setUnlocalizedName(name);
        setRegistryName(reg);
        setCreativeTab(Ref.tabModSquad);
        firstMessage = false;
    }

    private static boolean firstMessage;

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!firstMessage) {
            sendMessage(playerIn, "Downloads: " + Ref.downloads);
            firstMessage = true;
        } else {
            firstMessage = false;
        }

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    private static void sendMessage(EntityPlayer player, String msg) {
        player.addChatMessage(new TextComponentString(msg));
    }

}
