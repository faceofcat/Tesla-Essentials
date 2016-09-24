package com.trials.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import java.util.HashMap;
import java.util.Map;



public final class ChatSync {

    static final Map<String, ChatSync> INSTANCE = new HashMap<>();

    private final SimpleNetworkWrapper chatWrapper;

    private ChatSync(SimpleNetworkWrapper chatWrapper){ this.chatWrapper = chatWrapper; }

    public void sendPlayerChatMessage(EntityPlayerMP player, String message, int chatID){ chatWrapper.sendTo(new ChatMessage(message, chatID), player); }
    public void sendPlayerChatMessages(String message, int chatID, EntityPlayerMP... player){ for(EntityPlayerMP e : player) sendPlayerChatMessage(e, message, chatID); }
    public void sendDimensionChatMessage(WorldServer world, String message, int chatID){ for(EntityPlayerMP e : world.getEntities(EntityPlayerMP.class, å->true)) sendPlayerChatMessage(e, message, chatID); }

    // Factory
    public static void createChatSyncForMod(String modid, SimpleNetworkWrapper wrapper){
        if(INSTANCE.containsKey(modid)) return;
        wrapper.registerMessage(MessageHandler.class, ChatMessage.class, 1, Side.CLIENT);
        INSTANCE.put(modid, new ChatSync(wrapper));
    }

    public static ChatSync forMod(String mod){ return INSTANCE.get(mod); }

    // Classes
    public static class MessageHandler implements IMessageHandler<ChatMessage, IMessage>{

        @Override
        public ChatMessage onMessage(ChatMessage message, MessageContext ctx) {
            if(ctx.side.isClient()){
                FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().deleteChatLine(message.chatID);
                FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(
                        new TextComponentString(message.message), message.chatID);
            }
            return null;
        }
    }

    public static class ChatMessage implements IMessage{

        String message; // Not private to avoid synthetic accessor methods
        int chatID; // Not private to avoid synthetic accessor methods

        @SuppressWarnings("unused") // Used via reflection
        public ChatMessage(){}

        public ChatMessage(String message, int chatID){
            this.message = message;
            this.chatID = chatID;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            chatID = buf.readInt(); // Read chat id
            byte[] b = new byte[buf.readInt()]; // Allocate byte array based on defined message length
            buf.readBytes(b); // Read out message as bytes
            message = new String(b); // Convert and store
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(chatID); // Define chat id
            buf.writeInt(message.length()); // Define message length
            buf.writeBytes(message.getBytes()); // Serialize message
        }
    }
}
