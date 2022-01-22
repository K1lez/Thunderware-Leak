package com.thunderware.module.movement;

import org.lwjgl.input.Keyboard;

import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.NumberSetting;

public class LongJump extends ModuleBase {

	public NumberSetting speed = new NumberSetting("Speed", 0.5, 0.05, 0.15, 1);
	public boolean jumped;
	
	public LongJump() {
		super("LongJump", Keyboard.KEY_Z, Category.MOVEMENT);
		addSettings(speed);
	}
	
	public void onEnable() {
		jumped = false;
	}
	
	public void onDisable() {

	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			if(mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
				if(mc.thePlayer.onGround) {
					if(jumped == true) {
						this.toggle();
					}else {
						mc.thePlayer.jump();
						mc.thePlayer.motionY += 0.4;
					}
				}else {
					jumped = true;
				}
				mc.thePlayer.setSpeed(speed.getValue());
			}else {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
		}
	}

}
