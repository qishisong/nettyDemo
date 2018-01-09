package HttpTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 *
 * 首先向ChannelPipeline中添加HTTP请求消息解码器
 * 然后添加HttpObjectAggregator解码器，将多个消息转换为单一的FullHttpRequest或者FullHttpResponse,因为
 * HTTP解码器在每个HTTP消息中会生成多个消息对象
 * 然后添加HTTP响应编码器
 * 然后新增Chunked handler,主要作用是支持异步发送大的码流，但不占用过多的内存，防止发生java内存溢出错误
 * 最后添加业务逻辑处理
 *
 * @author qiss
 * @create 2017-12-20 16:17
 **/
public class HttpFileServer {
    private static final String DEFAULT_URL="/src/main/";

    public static void main(String[] args) throws Exception {
        int port=8080;
        if(args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //default port
            }
        }
        String url = DEFAULT_URL;
        if(args.length>1){
            url = args[1];
        }
        new HttpFileServer().run(port,url);
    }

    public void run(final int port,final String url) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap s = new ServerBootstrap();
            s.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture f = s.bind("127.0.0.1", port).sync();
            System.out.println("HTTP 文件目录服务器启动，网址是:http://127.0.0.1:"+port+url);
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
