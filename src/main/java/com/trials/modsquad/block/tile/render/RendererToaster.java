package com.trials.modsquad.block.tile.render;

import com.trials.modsquad.block.machine.BlockToaster;
import com.trials.modsquad.block.tile.TileToaster;
import com.trials.modsquad.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class RendererToaster extends TileEntitySpecialRenderer<TileToaster> {

    private static final EntityItem bread = new EntityItem(Minecraft.getMinecraft().theWorld, 0,0,0, new ItemStack(ModItems.breadSlice));
    private static final EntityItem toast = new EntityItem(Minecraft.getMinecraft().theWorld, 0,0,0, new ItemStack(ModItems.toastSlice));

    @Override
    public void renderTileEntityAt(TileToaster te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
        float a = 0.0625f;

        EntityItem slot1 = te.getToast1()?toast:bread;
        EntityItem slot2 = te.getToast2()?toast:bread;

        EnumFacing f;
        //Toasts need to be rotated more if this is true
        boolean rotated = (f=(EnumFacing) Minecraft.getMinecraft().theWorld.getBlockState(te.getPos()).getProperties().get(BlockToaster.PROPERTYFACING)).equals(EnumFacing.WEST) || f.equals(EnumFacing.EAST);
        if(te.getSlot1()) {
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(x, y, z);
                if(rotated) GL11.glRotated(90, 0, 1, 0);
                //GlStateManager.translate(5 * a, 2 * a, 6.75 * a);

                //GlStateManager.translate(0, 1 * a, 0);

                GlStateManager.scale(1.2, 1.2, 1.2);
                Minecraft.getMinecraft().getRenderManager().doRenderEntity(slot1, 0, 0, 0, 0f, 0f, false);

            }
            GlStateManager.popMatrix();
        }
        if(te.getSlot2()) {
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(x, y, z);
                if(rotated) GL11.glRotated(90, 0, 1, 0);

                //GlStateManager.translate(8.325 * a, 2 * a, 6.75 * a);

                //GlStateManager.rotate(rotated ? 25F : -9.8F, 0, 1, 0);

                //GlStateManager.translate(0, 1 * a, 0);

                GlStateManager.scale(1.2, 1.2, 1.2);
                Minecraft.getMinecraft().getRenderManager().doRenderEntity(slot2, 0, 0, 0, 0f, 0f, false);
            }
            GlStateManager.popMatrix();
        }
    }
}
