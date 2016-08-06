package com.trials.modsquad.block.TileEntities.renderer;

import com.trials.modsquad.block.Network.BasicCable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class CableRenderer extends TileEntitySpecialRenderer{
    float i = 0;
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        //Render
        //GlStateManager.rotate(90f, i=(i+1)%180, 0f, 0f);

        GlStateManager.popMatrix();
    }
}
