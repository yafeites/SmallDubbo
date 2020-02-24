package handler;

import context.ResponseReceiverHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);

    public NettyClientHandler() {
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseMessage response) throws Exception {
        // Netty异步获取结果后的操作:存入结果阻塞队列
        ResponseReceiverHolder.putResultValue(response);
        LOGGER.info("客户端接收返回结果:[content:{} id:{}]", response, response.getTraceId());
    }
}
