package com.trials.modsquad.gui;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.TileEntities.TileElectricFurnace;
import com.trials.modsquad.block.containers.ContainerElectricFurnace;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GUIElectricFurnace extends GuiContainer {

    private TileElectricFurnace furnace;

    public GUIElectricFurnace(InventoryPlayer player, TileElectricFurnace furnace) {
        super(new ContainerElectricFurnace(player, furnace));
        this.furnace = furnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Electric Furnace", 8, 6, 4210751);
        fontRendererObj.drawString("Power: "+ TeslaUtils.getDisplayableTeslaCount(furnace.getStoredPower()), 8, 18, 4210751);

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
