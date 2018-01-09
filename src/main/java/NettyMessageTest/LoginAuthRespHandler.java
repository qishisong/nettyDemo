package NettyMessageTest;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author qiss
 * @create 2017-12-27 17:41
 **/
public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        if(message.getHeader() != null && message.getHeader().getType() == (byte)1){
            System.out.println("Login is OK");
            String body = (String)message.getBody();
            System.out.println("Recevied message body from client is " + body);
        }
        ctx.writeAndFlush(buildLoginResponse((byte)3));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private NettyMessage buildLoginResponse(byte result){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte)2);
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
}
