package com.trials.modsquad.gui;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.TileEntities.TileFurnaceGenerator;
import com.trials.modsquad.block.containers.ContainerFurnaceGenerator;
import net.darkhax.tesla.lib.PowerBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GUIFurnaceGenerator extends GuiContainer {

    private TileFurnaceGenerator generator;
    private PowerBar p;

    public GUIFurnaceGenerator(InventoryPlayer player, TileFurnaceGenerator generator) {
        super(new ContainerFurnaceGenerator(player, generator));
        this.generator = generator;
        p = new PowerBar(this, xSize+100, 50, PowerBar.BackgroundType.LIGHT);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Furnace Generator", 8, 6, 4210751);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        ResourceLocation l;
        mc.renderEngine.getTexture(l=new ResourceLocation(ModSquad.MODID, "/textures/gui/container/fgen.png"));
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(l);
        drawTexturedModalRect((width - xSize)/2, (height-ySize)/2, 0, 0, xSize, ySize);
        p.draw(generator);
    }
}
