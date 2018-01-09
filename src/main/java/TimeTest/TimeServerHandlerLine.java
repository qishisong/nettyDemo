package TimeTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 *
 * ByteBuf类似于JDK中的java.nio.ByteBuffer对象，更加灵活强大。
 * 通过readableBytes方法可以获取缓冲去刻度的字节数，根据可读的字节数创建byte数组，
 * 通过readBytes方法将缓冲区中的字节数组复制到新建的byte数组中。
 *
 * ChannelHandlerContext中flush方法作用是将消息发送队列中的消息写入到SocketChannel中发送给对方
 * 从性能的角度考虑，为了防止频繁的唤醒Selector进行消息发送，Netty的write方法并不直接将消息写入SocketChannel中，
 * 调用write方法只是把待发送的消息放到发送缓冲数组中，再通过调用flush方法，将发送缓冲区的消息全部写到SocketChannel中
 *
 * @author Administrator
 * @date 2017/12/19
 */
public class TimeServerHandlerLine extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8").substring(0,req.length-System.getProperty("line.separator").length());*/
        String body = (String)msg;
        System.out.println("The time server receive order : "+body+";the cunter is : "+ ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime += System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        channelHandlerContext.close();
    }
}
