package com.trials.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileDataSync implements IMessage {

    private BlockPos pos;
    private String data;
    private int dim;

    @SuppressWarnings("unused") // Used through reflect
    public TileDataSync() {}

    public TileDataSync(BlockPos pos, String data, int dim){
        this.data = data;
        this.pos = pos;
        this.dim = dim;
        if(DimensionManager.getWorld(dim).getTileEntity(pos) instanceof Updatable)
            MinecraftForge.EVENT_BUS.register(this); // Technically a resource leak
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        this.data = new String(bytes);

        int x = Integer.parseInt(data.substring(0, data.indexOf(":")));
        int y = Integer.parseInt((data = data.substring(data.indexOf(":")+1)).substring(0, data.indexOf(":")));
        int z = Integer.parseInt((data = data.substring(data.indexOf(":")+1)).substring(0, data.indexOf(":")));
        pos = new BlockPos(x, y, z);

        dim = Integer.parseInt((data = data.substring(data.indexOf(":")+1)).substring(0, data.indexOf(":")));

        data = data.substring(data.indexOf(":")+1);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String s = pos.getX()+":"+pos.getY()+":"+pos.getZ()+":"+dim+":"+data;
        buf.writeBytes(Unpooled.wrappedBuffer(s.getBytes()));
    }

    public boolean sendToClassHandler(){
        Updatable e = (Updatable) Minecraft.getMinecraft().theWorld.getTileEntity(pos);
        if(e==null) return false;
        e.update(data);
        return true;
    }

    public static class Handler<T extends TileDataSync> extends AbstractClient<T>{

        @Override
        public IMessage handleClientMessage(TileDataSync message, MessageContext ctx) {
            if(FMLLaunchHandler.side().isClient()) message.sendToClassHandler();
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
    // Not used *yet*
    @SuppressWarnings("unused")
    public static abstract class AbstractServer<T extends IMessage> extends  AbstractBase<T>{
        @Override
        public final IMessage handleClientMessage(T message, MessageContext ctx) {
            return null;
        }
    }


}
