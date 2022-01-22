package com.thunderware.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.events.listeners.EventPacket;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.ModeSetting;

import net.minecraft.network.play.client.C03PacketPlayer;

public class Flight extends ModuleBase {

	public ModeSetting mode;
	
	public Flight() {
		super("Flight", Keyboard.KEY_F, Category.MOVEMENT);
		ArrayList<String> modes = new ArrayList<>();
		modes.add("Smooth");
		modes.add("BlocksMC");
		modes.add("Vanilla");
		mode = new ModeSetting("Mode", modes);
		addSettings(mode);
	}
	public static boolean hurt = false;
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			setSuffix(mode.getCurrentValue());
			switch(mode.getCurrentValue()) {
				case "Smooth":
					if(mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
						mc.thePlayer.setSpeed(0.5f);
					}else {
						mc.thePlayer.motionX /= 2;
						mc.thePlayer.motionZ /= 2;
					}
					if(mc.gameSettings.keyBindJump.isKeyDown())
						mc.thePlayer.motionY = 0.4;
					else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
						mc.thePlayer.motionY = -0.4;
					} else
						mc.thePlayer.motionY = 0;
					
					break;
					
					
				case "BlocksMC":
					if(mc.thePlayer.hurtTime != 0) {
						hurt = true;
					}
					if(hurt) {
						mc.thePlayer.motionY = 0.0;
						mc.thePlayer.setSpeed(3.0f);
						mc.timer.timerSpeed = 0.7f;
						((EventMotion) e).setOnGround(true);
					}
					break;
			}
		}
		
		////////////////////////////////////////////////////////
		
		if(e instanceof EventPacket) {
			if(mc.thePlayer == null)
				return;
			EventPacket event = (EventPacket)e;
			switch(mode.getCurrentValue()) {
			
				case "BlocksMC":
					
                    if (mc.thePlayer.ticksExisted % 3 != 0 && event.getPacket() instanceof C03PacketPlayer && hurt) {
                       event.setCancelled(true);
                    }
					
					break;
			}
		}
	}
	
	public void onEnable() {
		switch(mode.getCurrentValue()) {
			case "BlocksMC":
				double[] jumpYPositions = {0.41999998688698, 0.7531999805212, 1.00133597911214, 1.16610926093821, 1.24918707874468, 1.24918707874468, 1.1707870772188, 1.0155550727022, 0.78502770378924, 0.4807108763317, 0.10408037809304, 0.0};
				
				if(mc.thePlayer.onGround) {
					for(int i = 0; i < 4; i++) {
						mc.timer.timerSpeed = 0.0875f;
						for(double y : jumpYPositions) {
							mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY+y, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
						}
					}
					mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ,false));
				}
				
				break;
			case "Vanilla":
				hurt = true;
				break;
		}
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
		mc.thePlayer.motionX = 0;
		hurt = false;
		mc.thePlayer.motionZ = 0;
	}

}
