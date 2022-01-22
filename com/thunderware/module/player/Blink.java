package com.thunderware.module.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

import com.thunderware.Thunder;
import com.thunderware.events.Event;
import com.thunderware.events.EventDirection;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.events.listeners.EventPacket;
import com.thunderware.module.ModuleBase;
import com.thunderware.module.movement.Flight;
import com.thunderware.settings.settings.ModeSetting;
import com.thunderware.utils.TimerUtils;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Blink extends ModuleBase {

	public ModeSetting mode;
	public TimerUtils timer = new TimerUtils();
	public static ArrayList<Packet> packets = new ArrayList<Packet>();
	
	public Blink() {
		super("Blink", Keyboard.KEY_Z, Category.PLAYER);
	}
	
	public void onEvent(Event event) {
		if(event instanceof EventPacket) {
			if(mc.thePlayer == null)
				return;
			EventPacket e = (EventPacket)event;
			
			e.setCancelled(true);
			packets.add(e.getPacket());
		}
	}
	
	public void onDisable() {
		for(Packet p : packets) {
			if(p.getClass().getSimpleName().startsWith("C")) {
				mc.thePlayer.sendQueue.addToSendQueue(p);
			}else if(p.getClass().getSimpleName().contains("S12PacketEntityVelocity")) {
				mc.thePlayer.sendQueue.addToSendQueue(p);
			}
		}
		packets.clear();
	}
}
