package com.trials.modsquad.block.tile.render;

        import com.trials.modsquad.block.machine.BlockToaster;
        import com.trials.modsquad.block.tile.TileToaster;
        import com.trials.modsquad.item.ModItems;
        import net.minecraft.client.Minecraft;
        import net.minecraft.client.renderer.GlStateManager;
        import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
        import net.minecraft.entity.item.EntityItem;
        import net.minecraft.item.ItemStack;

public class RendererToaster extends TileEntitySpecialRenderer<TileToaster> {

    private static final EntityItem bread = new EntityItem(Minecraft.getMinecraft().theWorld, 0,0,0, new ItemStack(ModItems.breadSlice));
    private static final EntityItem toast = new EntityItem(Minecraft.getMinecraft().theWorld, 0,0,0, new ItemStack(ModItems.toastSlice));

    @Override
    public void renderTileEntityAt(TileToaster te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);

        float a = 0.0625f;

        EntityItem slot1 = bread;
        if (te.getToast1()) slot1 = toast;

        EntityItem slot2 = bread;
        if (te.getToast2()) slot2 = toast;

        BlockToaster.getStaticMetaFromState(Minecraft.getMinecraft().theWorld.getBlockState(te.getPos()));

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x,y,z);
            GlStateManager.scale(1.2,1.2,1.2);

            GlStateManager.translate(5*a,2*a,6.75*a);

            GlStateManager.rotate(75F, 0,1,0);

            if (te.getToast1()) {
                GlStateManager.rotate(-43F, 0,1,0);
                GlStateManager.translate(0,1*a,0);
            }

            if (te.getSlot1()) Minecraft.getMinecraft().getRenderManager().doRenderEntity(slot1, 0, 0, 0, 0f, 0f, false);

        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x,y,z);
            GlStateManager.scale(1.2,1.2,1.2);

            GlStateManager.translate(8.325*a,2*a,6.75*a);

            GlStateManager.rotate(75F, 0,1,0);

            if (te.getToast2()) {
                GlStateManager.rotate(-43F, 0,1,0);
                GlStateManager.translate(0,1*a,0);
            }

            if (te.getSlot2()) Minecraft.getMinecraft().getRenderManager().doRenderEntity(slot2, 0, 0, 0, 0f, 0f, false);
        }
        GlStateManager.popMatrix();
    }
}
