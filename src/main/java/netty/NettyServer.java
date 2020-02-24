package netty;

import Utils.ZkConfigHelper;
import coder.NettyDecoderHandler;
import coder.NettyEncoderHandler;
import exception.CloseNettyException;
import handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import message.RequestMessage;

public class NettyServer {
    /**
     * 服务端boss线程组
     */
    private EventLoopGroup bossGroup;
    /**
     * 服务端worker线程组
     */
    private EventLoopGroup workerGroup;
    /**
     * 绑定端口的Channel
     */
    private Channel channel;

    /**
     * 启动Netty服务：同步方法，因为本机中可能有多个服务绑定在同一个端口上，会并发创建server并start
     */
    public void start(final int port) {
        if (bossGroup != null || workerGroup != null) {
            return;
        }
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        String serialize = ZkConfigHelper.getSerializer();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 注册接收消息解码器
                        ch.pipeline().addLast(new NettyDecoderHandler(RequestMessage.class));
                        // 注册返回消息编码器
                        ch.pipeline().addLast(new NettyEncoderHandler(serialize));
                        // 注册服务端业务逻辑处理器
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                });
        try {
            channel = serverBootstrap.bind(port).sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void stop() throws  CloseNettyException{
        if (null == channel) {
            throw new CloseNettyException("Netty Server Stoped");
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }

}
