package com.thunderware.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import com.thunderware.Thunder;
import com.thunderware.events.Event;
import com.thunderware.events.EventType;
import com.thunderware.events.listeners.EventMotion;
import com.thunderware.module.ModuleBase;
import com.thunderware.settings.settings.BooleanSetting;
import com.thunderware.settings.settings.ModeSetting;
import com.thunderware.settings.settings.NumberSetting;
import com.thunderware.utils.EntityUtils;
import com.thunderware.utils.TimerUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class Killaura extends ModuleBase {

	public NumberSetting cps = new NumberSetting("CPS", 9.5, 0.25, 1, 20);
	public NumberSetting reach = new NumberSetting("Reach", 4.2, 0.1, 2, 6);
	public ModeSetting timing;
	public ModeSetting rotations;
	public BooleanSetting keepSprint = new BooleanSetting("KeepSprint",true);
	public float[] rots;
	
	public Killaura() {
		super("KillAura", Keyboard.KEY_V,Category.COMBAT);
		ArrayList<String> times = new ArrayList<>();
		times.add("PRE");
		times.add("POST");
		this.timing = new ModeSetting("Timing", times);
		ArrayList<String> rotations = new ArrayList<>();
		rotations.add("NCP");
		rotations.add("AGC");
		rotations.add("None");
		this.rotations = new ModeSetting("Rotations", rotations);
		addSettings(timing, this.rotations, keepSprint);
		addSettings(cps, reach);
	}
	
	private TimerUtils timer = new TimerUtils();
	public static Entity currentTarget;
	
	public void onEvent(Event baseEvent) {
		if(baseEvent instanceof EventMotion && mc.thePlayer.ticksExisted > 40) {
			setSuffix(Math.floor(cps.getValue()) + " | " + Math.floor(reach.getValue()));
			EventMotion event = (EventMotion)baseEvent;
			CopyOnWriteArrayList<Entity> ent = AntiBot.getEntities();
			Entity target = getMainEntity(EntityUtils.distanceSort(ent));
			rots = ncpRotations(target, event);
			if(target != null) {
				if(rotations.getCurrentValue() == "NCP") {
					rots = ncpRotations(target, event);
				}else if(rotations.getCurrentValue() == "AGC") {
					rots = testRotations(target, event);
				}
				if(target.getDistanceToEntity(mc.thePlayer) <= (reach.getValue() + 1)) {
					currentTarget = target;
					//if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
						//mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 2);
					if(rotations.getCurrentValue() != "None") {
						event.yaw = rots[0];
						event.pitch = rots[1];
					}
					
					mc.thePlayer.rotationYawHead = event.getYaw();
				}else {
					currentTarget = null;
				}
				if(target.getDistanceToEntity(mc.thePlayer) <= reach.getValue() && (timer.hasReached((cps.getValue() + (Math.random() * 2.2)) / 1000) || cps.getValue() > 19.95)) {
					if(timing.getCurrentValue() == "PRE" && event.eventType == EventType.PRE) {
						mc.thePlayer.swingItem();
						if(keepSprint.getValue()) {
							mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C02PacketUseEntity(target,C02PacketUseEntity.Action.ATTACK));
						}else {
							mc.playerController.attackEntity(mc.thePlayer, target);
						}
					}else if(timing.getCurrentValue() == "POST" && event.eventType == EventType.POST) {
						mc.thePlayer.swingItem();
						if(keepSprint.getValue()) {
							mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C02PacketUseEntity(target,C02PacketUseEntity.Action.ATTACK));
						}else {
							mc.playerController.attackEntity(mc.thePlayer, target);
						}
					}
					
					timer.reset();
				}
			}else {
				currentTarget = null;
			}
		}
	}

	public Entity getMainEntity(CopyOnWriteArrayList<Entity> entities) {
		if(entities.size() > 0)
			for(Entity ent : entities) {
				if(ent != mc.thePlayer && ent instanceof EntityLivingBase && ent.isEntityAlive()) {
					if(ent instanceof EntityPlayer) {
						return ent;
					}
					
					//Delete Line Below When Finished
					return ent;
				}
			}
		
		return null;
	}
	
	/*
	 * From Old Client <3
	*/
	public static float[] ncpRotations(Entity e, EventMotion p) {
		double x = e.posX + (e.posX - e.lastTickPosX) - p.getX();
		double y = (e.posY + e.getEyeHeight()) - (p.getY() + Minecraft.getMinecraft().thePlayer.getEyeHeight()) - 0.1;
		double z = e.posZ + (e.posZ - e.lastTickPosZ) - p.getZ();
		double dist = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

		float yaw = (float) Math.toDegrees(-Math.atan(x / z));
		float pitch = (float) -Math.toDegrees(Math.atan(y / dist));

		if (x < 0 && z < 0)
			yaw = 90 + (float) Math.toDegrees(Math.atan(z / x));
		else if (x > 0 && z < 0)
			yaw = -90 + (float) Math.toDegrees(Math.atan(z / x));

		yaw += Math.random() * 4 - Math.random();
		pitch += Math.random() * 4 - Math.random();

		if (pitch > 90)
			pitch = 90;
		if (pitch < -90)
			pitch = -90;
		if (yaw > 180)
			yaw = 180;
		if (yaw < -180)
			yaw = -180;

		return new float[]{yaw, pitch};
	}
	
	public static float[] testRotations(Entity e, EventMotion p) {
		double x = e.posX + (e.posX - e.lastTickPosX) - p.getX();
		double y = (e.posY + e.getEyeHeight()) - (p.getY() + Minecraft.getMinecraft().thePlayer.getEyeHeight()) - 0.1;
		double z = e.posZ + (e.posZ - e.lastTickPosZ) - p.getZ();
		double dist = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

		float yaw = (float) Math.toDegrees(-Math.atan(x / z));
		float pitch = (float) -Math.toDegrees(Math.atan(y / dist));
		
		if (x < 0 && z < 0)
			yaw = 90 + (float) Math.toDegrees(Math.atan(z / x));
		else if (x > 0 && z < 0)
			yaw = -90 + (float) Math.toDegrees(Math.atan(z / x));
		
		float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float gcd = f * f * f * 1.2F;

		yaw -= yaw % gcd;
		pitch -= pitch % gcd;

		yaw += Math.random() * (Math.random()*6) - Math.random();
		pitch += Math.random() * (Math.random()*(6-Math.random())) - Math.random();

		if (pitch > 90)
			pitch = 90;
		if (pitch < -90)
			pitch = -90;
		if (yaw > 180)
			yaw = 180;
		if (yaw < -180)
			yaw = -180;

		return new float[]{yaw, pitch};
	}
	
}
