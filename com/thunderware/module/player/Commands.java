package com.thunderware.module.player;

import java.util.Locale;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.thunderware.Thunder;
import com.thunderware.events.Event;
import com.thunderware.events.listeners.EventChat;
import com.thunderware.module.ModuleBase;

import net.minecraft.util.ChatComponentText;

public class Commands extends ModuleBase {

    public Commands() {
        super("Commands", Keyboard.KEY_NONE, Category.PLAYER);
    }

    public void onEvent(Event e) {
        if(e instanceof EventChat) {
            String message = ((EventChat) e).getMessage();
            String prefix = ".";

            if(message.startsWith(prefix)) {
                try {
                    String[] paramArray = message.split(" ");

                    if (paramArray[0].contains("bind")) {
                        Thunder.moduleManager.getModuleByName(paramArray[1]).setKey(Keyboard.getKeyIndex(paramArray[2].toUpperCase(Locale.ROOT)));
                    }
                } catch(Exception exception) {
                    mc.thePlayer.addChatMessage(new ChatComponentText(ChatFormatting.DARK_PURPLE + "[Thunderware] " + ChatFormatting.YELLOW + "Syntax error."));
                }

                e.setCancelled(true);
            }
        }
    }
}