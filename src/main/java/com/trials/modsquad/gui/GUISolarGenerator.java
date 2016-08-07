package com.trials.modsquad.gui;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.TileEntities.TileSolarPanel;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GUISolarGenerator extends GuiScreen {

    private ITeslaHolder solarPanel;
    private PowerBar p;

    public GUISolarGenerator(TileSolarPanel solarPanel) {
        this.solarPanel = solarPanel.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        p = new PowerBar(this, +100, 50, PowerBar.BackgroundType.LIGHT);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String power;
        int count = (power = TeslaUtils.getDisplayableTeslaCount(solarPanel.getStoredPower())).length();
        fontRendererObj.drawString(power, width-45-count/2, 35, 4210751);
        p.draw(solarPanel);
    }

    @Override
    public void initGui() {
        ResourceLocation l;
        mc.renderEngine.getTexture(l=new ResourceLocation(ModSquad.MODID, "textures/gui/container/fgen.png"));
        mc.renderEngine.bindTexture(l);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawTexturedModalRect((width)/2, (height)/2, 0, 0, width, height);
        fontRendererObj.drawString("Solar Generator", 8, 6, 4210751);
    }
}
