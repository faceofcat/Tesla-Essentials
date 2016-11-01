package com.trials.modsquad.gui;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.tile.TileGrinder;
import com.trials.modsquad.block.container.ContainerGrinder;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

public class GUIGrinder extends GuiContainer{
    public static ResourceLocation grinderGUI = new ResourceLocation(ModSquad.MODID, "textures/gui/container/grinder.png");

    private TileGrinder grinder;


    public GUIGrinder(InventoryPlayer player, TileGrinder grinder) {
        super(new ContainerGrinder(player, grinder));
        this.grinder = grinder; // grinder.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Grinder", 8, 6, 4210751);
        ITeslaHolder tesla = this.grinder.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (tesla != null) {
            String power = TeslaUtils.getDisplayableTeslaCount(tesla.getStoredPower());
            String max = TeslaUtils.getDisplayableTeslaCount(tesla.getCapacity());
            fontRendererObj.drawString(power + " / " + max, 8, 72, 4210751);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.renderEngine.getTexture(grinderGUI);
        mc.renderEngine.bindTexture(grinderGUI);
        GL11.glColor4f(1f, 1f, 1f, 1f);

        drawTexturedModalRect(super.guiLeft, super.guiTop, 0, 0, xSize, ySize);
        if (this.grinder.getIsGrinding()) {
            drawTexturedModalRect(super.guiLeft + 80, super.guiTop + 35, 177, 14, Math.round(22.0f * this.grinder.getGrinderProgress() / 100.0f), 16);
        }

        ITeslaHolder tesla = this.grinder.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (tesla != null) {
            PowerBar bar = new PowerBar(this, super.guiLeft + 8, super.guiTop + 34 - (PowerBar.HEIGHT - 18) / 2, PowerBar.BackgroundType.LIGHT);
            bar.draw(tesla);
        }
    }
}
