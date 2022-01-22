package com.thunderware.module.movement;

import org.lwjgl.input.Keyboard;

import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.NumberSetting;

import net.minecraft.util.BlockPos;

public class AntiVoid extends ModuleBase {

	public AntiVoid() {
		super("AntiVoid", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onEnable() {

	}
	
	public void onDisable() {

	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			if(mc.thePlayer.fallDistance > 4.0115) {
				int dumb = 0;
				for(int i = (int)mc.thePlayer.posY; i > 0; i--) {
					if(mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX,i,mc.thePlayer.posZ))) {
						dumb++;
					}
				}
				if(dumb > (int)mc.thePlayer.posY-1) {
					((EventMotion) e).setOnGround(true);
					mc.thePlayer.fallDistance = 0.0F;
					mc.thePlayer.jump();
					mc.thePlayer.motionY = 1.0;
					mc.thePlayer.setSpeed(0.36F);
				}
			}
		}
	}

}
