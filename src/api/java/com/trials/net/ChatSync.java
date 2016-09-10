package com.trials.net;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.lang.reflect.Field;

public final class ChatSync {

    public static final ChatSync INSTANCE = null;

    private SimpleNetworkWrapper chatWrapper;

    ChatSync(SimpleNetworkWrapper chatWrapper){
        this.chatWrapper = chatWrapper;

        try{
            Field f = ChatSync.class.getDeclaredField("INSTANCE");
            f.setAccessible(true);
            f.set(null, this); // Technically a resource leak since a reference is available to a not-fully-constructed object
        }catch(Exception e){}
    }

    public void sendPlayerChatMessage(String message, int chatID){

    }
}
