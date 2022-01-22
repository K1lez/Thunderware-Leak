package com.thunderware.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.ModeSetting;
import com.thunderware.settings.settings.NumberSetting;

public class Speed extends ModuleBase {

	public NumberSetting speed = new NumberSetting("Speed", 0.5, 0.05, 0.15, 1);
	public ModeSetting mode;
	public static int ticks = 0;
	
	public Speed() {
		super("Speed", Keyboard.KEY_X, Category.MOVEMENT);
		ArrayList<String> modes = new ArrayList<>();
		modes.add("Vanilla");
		modes.add("BlocksMC");
		modes.add("Verus");
		modes.add("Matrix");
		modes.add("Hypixel");
		modes.add("TimerBMC");
		this.mode = new ModeSetting("Mode", modes);
		addSettings(mode);
		addSettings(speed);
	}
	
	public void onEnable() {
		ticks = 0;
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
		mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
		mc.thePlayer.jumpMovementFactor = 0.02f;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			switch(mode.getCurrentValue()) {
				case "Vanilla":
					if(mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
						mc.gameSettings.keyBindJump.pressed = false;
						if(mc.thePlayer.onGround)
							mc.gameSettings.keyBindJump.pressed = true;
						mc.thePlayer.setSpeed(speed.getValue());
					}else {
						mc.thePlayer.motionX = 0;
						mc.thePlayer.motionZ = 0;
					}
					
					break;
					
				case "Verus":
					if (mc.thePlayer.onGround) {
	                    if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
	                        mc.thePlayer.jump();
	                    }
	                    mc.timer.timerSpeed = 1.0f;
	                    mc.thePlayer.setSpeed(0.61F);
	                } else {
	                    mc.thePlayer.setSpeed(0.36F);
	                }
					if(mc.thePlayer.hurtTime != 0) {
						mc.thePlayer.setSpeed(0.45f);
					}
					
					break;
					
				case "BlocksMC":
					if (mc.thePlayer.onGround) {
	                    if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
	                        mc.thePlayer.jump();
	                    }
	                    mc.timer.timerSpeed = 1.0f;
	                    mc.thePlayer.setSpeed(0.61F);
	                } else {
	                    mc.thePlayer.setSpeed(0.45F);
	                    if(mc.thePlayer.ticksExisted % 2 != 0) {
	                    	mc.thePlayer.setSpeed(speed.getValue());
	                    }
	                }
					if(mc.thePlayer.hurtTime != 0) {
						mc.thePlayer.setSpeed(0.9f);
					}
					
					break;
				
				case "Matrix":
					if (mc.thePlayer.onGround) {
	                    if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
	                    	mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
	                        mc.gameSettings.keyBindJump.pressed = true;
	                        mc.timer.timerSpeed = 21.7f;
	                    }
	                }else {
	                	mc.timer.timerSpeed = 1.0f;
	                }
					
					break;
					
				case "Hypixel":
					if (mc.thePlayer.onGround) {
						mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
						mc.timer.timerSpeed = 1.3f;
	                }else {
	                	mc.timer.timerSpeed = 1.0f;
	                }
					
					break;
					
				case "TimerBMC":
					if (mc.thePlayer.onGround) {
	                    if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
	                        mc.thePlayer.jump();
	                    }
	                    mc.timer.timerSpeed = 1.33f;
	                    mc.thePlayer.setSpeed(0.61F);
	                } else {
	                    mc.thePlayer.setSpeed(0.45F);
	                    if(mc.thePlayer.ticksExisted % 2 != 0) {
	                    	mc.thePlayer.setSpeed(speed.getValue());
	                    }
	                }
					if(mc.thePlayer.hurtTime != 0) {
						mc.thePlayer.setSpeed(0.9f);
					}
					
					break;
			}
		}
	}

}
