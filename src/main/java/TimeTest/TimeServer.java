package TimeTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 *
 * NioEventLoopGroup是个线程组，它包含一组NIO线程，专门用于网络事件的处理，实际上为NIO中reactor线程组
 * ServerBootstrap为Netty启动NIO服务端的辅助启动类，目的是降低服务端的开发负责度
 * NioServerSocketChannel对用于JDK NIO类库中的ServerSocketChannel类
 * ChildChannelHandler作用类似于Reactor模式中的handler类
 * ChannelFuture作用类似于JDK的java.util.concurrent.Future，主要用于异步操作的通知回调
 * @author Administrator
 * @date 2017/12/19
 */
public class TimeServer {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args != null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采取默认值
            }
        }
        new TimeServer().bind(port);
    }

    public void bind(int port) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChildChannelHandler());

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * LineBasedFrameDecoder的工作原理是依次遍历ByteBuf中的可读字节，判读看是否有“\n”或“\r\n”，如果有，
     * 就以此位置为结束位置，从可读索引到结束位置区间的字节就组成了一行。它是以换行符为结束标志的解码器，支持携带
     * 结束符或者不携带结束符两种解码方式，同时支持配置单行的最大长度。如果连续读取到最大长度后仍然没有发现换行符，
     * 就抛出异常，同时忽略掉之前的异常码
     *
     * StringDecoder为将接受到的对象转换成字符串
     */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024)); //解决TCP粘包问题
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast(new TimeServerHandlerLine());
        }
    }
}
