package com.trials.modsquad.gui;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.TileEntities.TileGrinder;
import com.trials.modsquad.block.containers.ContainerGrinder;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GUICharger extends GuiContainer{

    private TileGrinder grinder;

    public GUICharger(InventoryPlayer player, TileGrinder grinder) {
        super(new ContainerGrinder(player, grinder));
        this.grinder = grinder;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Grinder", 8, 6, 4210751);
        fontRendererObj.drawString("Power: "+ TeslaUtils.getDisplayableTeslaCount(grinder.getStoredPower()), 8, 18, 4210751);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        ResourceLocation l;
        mc.renderEngine.getTexture(l=new ResourceLocation(ModSquad.MODID, "/textures/gui/container/grinder.png"));
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(l);
        drawTexturedModalRect((width - xSize)/2, (height-ySize)/2, 0, 0, xSize, ySize);
    }
}
