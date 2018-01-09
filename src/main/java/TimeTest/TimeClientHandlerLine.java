package TimeTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 当客户端和服务端TCP链路建立成功后，Netty的NIO线程会调用channelActive方法，发送查询时间的指令
 * 给服务端，调用ChannelHandlerContext的writeAndFlush方法将请求消息发送给服务端
 *
 * @author Administrator
 * @date 2017/12/19
 */
public class TimeClientHandlerLine extends ChannelHandlerAdapter {
    private static  final Logger logger = LoggerFactory.getLogger(TimeClientHandlerLine.class);

    private int counter;
    private byte[] req;

    public TimeClientHandlerLine(){
       req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ByteBuf message = null;
        for (int i = 0; i < 70; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");*/
        String body = (String)msg;
        System.out.println("Now is : "+body+";the counter is : "+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected from downstream : "+cause.getMessage());
        ctx.close();
    }
}
