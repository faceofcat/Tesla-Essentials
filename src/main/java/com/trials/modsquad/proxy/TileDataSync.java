package com.trials.modsquad.proxy;

import com.trials.modsquad.block.TileEntities.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TileDataSync implements IMessage {

    /**
     * Order that sub-messages are ordered in. USE THIS WHEN GETTING SOMETHING THAT ISN'T A BYTE FROM THE MESSAGE!
     */
    public static final ByteOrder order = ByteOrder.LITTLE_ENDIAN;
    /**
     * Class reference for where to call "onClientEvent(FMLNetworkEvent.ClientCustomPacketEvent)"
     */
    public static final Class<? extends TileEntity>[] tileRef = new Class[]{TileGrinder.class, TileFurnaceGenerator.class, TileCapacitor.class, TileCharger.class, TileSolarPanel.class};
    private int classIndex;
    private BlockPos pos;
    private Class<? extends TileEntity> clazz;
    private String data;

    public TileDataSync(){}

    public TileDataSync(int index, BlockPos pos, String data){
        clazz = tileRef[classIndex = index];
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        clazz = tileRef[classIndex = buf.readInt()];
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        this.data = new String(bytes);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBuffer buffer = ByteBuffer.allocate(24+data.getBytes().length);
        buffer.putInt(classIndex);
        buffer.putInt(pos.getX());
        buffer.putInt(pos.getY());
        buffer.putInt(pos.getZ());
        buffer.put(data.getBytes());
        buf.writeBytes(Unpooled.wrappedBuffer(buffer));
    }

    public boolean sendToClassHandler(){
        TileEntity e = Minecraft.getMinecraft().theWorld.getTileEntity(pos);
        if(e==null) return false;
        try{
            Method m = clazz.getDeclaredMethod("updateNBT");
            m.setAccessible(true);
            m.invoke(e, JsonToNBT.getTagFromJson(data));
        }catch(Exception e1){ return false; }
        return true;
    }

    public static class Handler<T extends TileDataSync> extends AbstractClient<T>{

        @Override
        public IMessage handleClientMessage(TileDataSync message, MessageContext ctx) {
            message.sendToClassHandler();
            return null;
        }
    }

    public static abstract class AbstractBase<T extends IMessage> implements  IMessageHandler<T, IMessage>{
        @SideOnly(Side.CLIENT)
        public abstract IMessage handleClientMessage(T message, MessageContext ctx);
        @SideOnly(Side.SERVER)
        public abstract IMessage handleServerMessage(T message, MessageContext ctx);

        @Override
        public IMessage onMessage(T message, MessageContext ctx) {
            if(ctx.side.isClient()) return handleClientMessage(message, ctx);
            else return handleServerMessage(message, ctx);
        }
    }

    public static abstract class AbstractClient<T extends IMessage> extends AbstractBase<T>{
        @Override
        public final IMessage handleServerMessage(T message, MessageContext ctx) {
            return null;
        }
    }
    public static abstract class AbstractServer<T extends IMessage> extends  AbstractBase<T>{
        @Override
        public final IMessage handleClientMessage(T message, MessageContext ctx) {
            return null;
        }
    }


}
