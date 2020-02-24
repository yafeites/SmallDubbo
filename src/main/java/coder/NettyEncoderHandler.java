package coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import serializer.SerializerAdapter;

public class NettyEncoderHandler extends MessageToByteEncoder {
    private String serializerType;
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        int serializerCode = SerializerAdapter.getCodeByType(serializerType);
        out.writeInt(serializerCode);
        // 将对象序列化为字节数组
        byte[] data = SerializerAdapter.serialize(msg, serializerType);
        // 将字节数组(消息体)的长度作为消息头写入,解决半包/粘包问题
        out.writeInt(data.length);
        // 最后才写入序列化后得到的字节数组
        out.writeBytes(data);
    }

    public NettyEncoderHandler(String serializerType) {
        this.serializerType = serializerType;
    }

}
