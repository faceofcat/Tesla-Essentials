package com.trials.modsquad.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GUIBook extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int page = 1;
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    GuiButton next;
    GuiButton back;

    @Override
    public void initGui() {
        page = 1;
        this.drawDefaultBackground();
        this.buttonList.add(this.next = new GuiButton(0, this.width, 20, ">"));
        this.buttonList.add(this.back = new GuiButton(0, 20, 20, "<"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        int page;
        if (button == this.next) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                page++;
                //do stuff
            }
        } else if (button == this.back) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                page--;
                //do stuff
            }

        }
        System.out.println("Page: " + page);
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
