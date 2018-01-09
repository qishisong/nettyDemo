package WebSocketTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author qiss
 * @create 2017-12-25 10:29
 **/
public class WebSocketServer {

    public static void main(String[] args) throws Exception {
        int port=8080;
        if(args.length > 0){
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //default port
            }
        }
        new WebSocketServer().run(port);
    }

    public void run(int port) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap s = new ServerBootstrap();
            s.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("http-codec",new HttpServerCodec())
                                    .addLast("aggregator",new HttpObjectAggregator(65536))
                                    .addLast("http-chunked",new ChunkedWriteHandler())
                                    .addLast("handler",new WebSocketServerHandler());
                        }
                    });
            Channel ch = s.bind(port).sync().channel();
            System.out.println("Web socket server started at port "+port+".");
            System.out.println("Open your broeser and nabigate to http://localhost:"+port+"/");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
