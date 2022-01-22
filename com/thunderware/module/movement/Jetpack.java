package com.thunderware.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.events.listeners.EventPacket;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.ModeSetting;

import net.minecraft.network.play.client.C03PacketPlayer;

public class Jetpack extends ModuleBase {

	public ModeSetting mode;
	
	public Jetpack() {
		super("Jetpack", Keyboard.KEY_NONE, Category.MOVEMENT);
		ArrayList<String> modes = new ArrayList<>();
		modes.add("Vanilla");
		modes.add("BlocksMC");
		mode = new ModeSetting("Mode", modes);
		addSettings(mode);
	}
	public static boolean hurt = false;
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			setSuffix(mode.getCurrentValue());
			switch(mode.getCurrentValue()) {
				case "Vanilla":
					if(mc.gameSettings.keyBindJump.isKeyDown()) {
						mc.thePlayer.jump();
					}
					
					break;
					
					
				case "BlocksMC":
					if(mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.ticksExisted % 2 == 0) {
						mc.thePlayer.jump();
						mc.thePlayer.setSpeed(0.221f);
					}
					break;
			}
		}
		
		////////////////////////////////////////////////////////
		
		if(e instanceof EventPacket) {
			if(mc.thePlayer == null)
				return;
			EventPacket event = (EventPacket)e;
		}
	}
	
	public void onEnable() {
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
		mc.thePlayer.motionX = 0;
		hurt = false;
		mc.thePlayer.motionZ = 0;
	}

}
