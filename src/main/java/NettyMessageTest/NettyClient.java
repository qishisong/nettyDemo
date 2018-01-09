package NettyMessageTest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author qiss
 * @create 2017-12-27 17:58
 **/
public class NettyClient {


    public static void main(String[] args) throws Exception {
        new NettyClient().connect("127.0.0.1",8080);
    }

    public void connect(String remoteServer, int port) throws Exception {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup).channel(NioSocketChannel.class).handler(new ChildChannelHandler());
            ChannelFuture f = b.connect(remoteServer, port).sync();
            System.out.println("Netty time Client conenected at port "+port);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4,-8,0))
                    .addLast(new NettyMessageEncoder())
                    .addLast(new LoginAuthReqHandler());
        }
    }
}
