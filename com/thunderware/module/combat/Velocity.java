package com.thunderware.module.combat;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.thunderware.Thunder;
import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.events.listeners.EventPacket;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.ModeSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends ModuleBase {

	public ModeSetting mode;
	
	public Velocity() {
		super("Velocity", Keyboard.KEY_NONE, Category.COMBAT);
		ArrayList<String> modes = new ArrayList<>();
		modes.add("Cancel");
		modes.add("AGC");
		this.mode = new ModeSetting("Mode", modes);
		addSettings(mode);
	}
	
	public void onEvent(Event event) {
		if(event instanceof EventPacket) {
			if(mc.thePlayer == null)
				return;
			EventPacket e = (EventPacket)event;
			switch(mode.getCurrentValue()) {
			
				case "Cancel":
					if(e.getPacket() instanceof S12PacketEntityVelocity)
						e.setCancelled(true);
					
			}
			
			if(e.getPacket() instanceof S27PacketExplosion)
	            e.setCancelled(true);
			
					
					
					
			}
		{
		}
		
		if(event instanceof EventMotion) {
			if(mc.thePlayer == null)
				return;
			EventMotion e = (EventMotion)event;
			switch(mode.getCurrentValue()) {
			
				case "AGC":
					if(mc.thePlayer.hurtTime == 9) {
						mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
					}
					
					break;
			}
		}
	}
}
