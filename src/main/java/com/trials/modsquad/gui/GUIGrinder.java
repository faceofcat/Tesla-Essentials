package com.trials.modsquad.gui;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.TileEntities.TileGrinder;
import com.trials.modsquad.block.containers.ContainerGrinder;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GUIGrinder extends GuiContainer{

    public static ResourceLocation grinderGUI = new ResourceLocation(ModSquad.MODID, "textures/gui/container/grinder.png");
    private ITeslaHolder grinder;
    private PowerBar p;

    public GUIGrinder(InventoryPlayer player, TileGrinder grinder) {
        super(new ContainerGrinder(player, grinder));
        this.grinder = grinder.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        p = new PowerBar(this, xSize+20, ySize+10, PowerBar.BackgroundType.LIGHT);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Grinder", 8, 6, 4210751);
        String power;
        int count = (power = TeslaUtils.getDisplayableTeslaCount(grinder.getStoredPower())).length();
        fontRendererObj.drawString(power, xSize-45-count/2, 35, 4210751);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.renderEngine.getTexture(grinderGUI);
        mc.renderEngine.bindTexture(grinderGUI);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawTexturedModalRect((width - xSize)/2, (height-ySize)/2, 0, 0, xSize, ySize);
        p.draw(grinder);
    }
}
