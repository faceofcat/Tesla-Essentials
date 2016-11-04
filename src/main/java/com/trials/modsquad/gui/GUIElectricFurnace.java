package com.trials.modsquad.gui;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.tile.TileElectricFurnace;
import com.trials.modsquad.block.container.ContainerElectricFurnace;
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

public class GUIElectricFurnace extends GuiContainer {
    private TileElectricFurnace furnace;
    // private PowerBar p;
    // private Field x;
    // private Field y;

    public GUIElectricFurnace(InventoryPlayer player, TileElectricFurnace furnace) {
        super(new ContainerElectricFurnace(player, furnace));
        this.furnace = furnace; // .getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
//        p = new PowerBar(this, ((width -xSize)/2) + 8, ((height-ySize)/2)+15, PowerBar.BackgroundType.LIGHT);
//        try {
//            x = PowerBar.class.getDeclaredField("x");
//            y = PowerBar.class.getDeclaredField("y");
//            x.setAccessible(true);
//            y.setAccessible(true);
//        } catch (NoSuchFieldException e) {}
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Electric Furnace", 8, 6, 4210751);
        ITeslaHolder tesla = this.furnace.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (tesla != null) {
            String power = TeslaUtils.getDisplayableTeslaCount(tesla.getStoredPower());
            String max = TeslaUtils.getDisplayableTeslaCount(tesla.getCapacity());
            fontRendererObj.drawString(power + " / " + max, 8, 72, 4210751);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = new ResourceLocation(ModSquad.MODID, "textures/gui/container/grinder.png");
        mc.renderEngine.getTexture(texture);
        mc.renderEngine.bindTexture(texture);

        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawTexturedModalRect(super.guiLeft, super.guiTop, 0, 0, xSize, ySize);
        if (this.furnace.getIsSmelting()) {
            drawTexturedModalRect(super.guiLeft + 80, super.guiTop + 35, 177, 14, Math.round(22.0f * this.furnace.getSmeltProgress() / 100.0f), 16);
            drawTexturedModalRect(super.guiLeft + 57, super.guiTop + 53, 176, 0, 14, 14);
        }

        ITeslaHolder tesla = this.furnace.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (tesla != null) {
            PowerBar bar = new PowerBar(this, super.guiLeft + 8, super.guiTop + 34 - (PowerBar.HEIGHT - 18) / 2, PowerBar.BackgroundType.LIGHT);
            bar.draw(tesla);
        }
    }
}
