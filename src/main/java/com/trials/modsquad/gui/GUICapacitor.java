package com.trials.modsquad.gui;

import com.trials.modsquad.ModSquad;
import com.trials.modsquad.block.TileEntities.TileCapacitor;
import com.trials.modsquad.block.TileEntities.TileCharger;
import com.trials.modsquad.block.containers.ContainerCharger;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static com.trials.modsquad.block.ModBlocks.charger;

public class GUICapacitor extends GuiScreen {

    private ITeslaHolder capacitor;
    private PowerBar p;

    public GUICapacitor(InventoryPlayer player, TileCapacitor capacitor) {

        this.capacitor = capacitor.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        p = new PowerBar(this, this.width / 2, this.height / 2 + 25, PowerBar.BackgroundType.LIGHT);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        fontRendererObj.drawString("Capacitor", 8, 6, 4210751);
        String power;
        int count = (power = TeslaUtils.getDisplayableTeslaCount(capacitor.getStoredPower())).length();
        fontRendererObj.drawString(power, this.width-45-count/2, 35, 4210751);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        p.draw(capacitor);
        super.updateScreen();
    }

}
