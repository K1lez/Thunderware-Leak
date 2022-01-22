package com.thunderware.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.ModeSetting;

import net.minecraft.network.play.client.C03PacketPlayer;

public class Phase extends ModuleBase {
	
	public Phase() {
		super("Phase", Keyboard.KEY_L, Category.PLAYER);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			mc.thePlayer.motionY = -74F;
		}
	}
}
