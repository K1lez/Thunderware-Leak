package com.thunderware.module.visuals;

import org.lwjgl.opengl.GL11;

import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventRender2D;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.NumberSetting;
import com.thunderware.utils.RenderUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class Ambience extends ModuleBase {

	public NumberSetting time = new NumberSetting("Time", 16000, 1, 0, 24000);
	
	public Ambience() {
		super("Ambience", 0, Category.VISUALS);
		addSettings(time);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventRender2D) {
			mc.theWorld.setWorldTime(time.getValue().intValue());
		}
	}

}
