package coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import serializer.SerializerAdapter;

import java.util.List;

public class NettyDecoderHandler  extends ByteToMessageDecoder {
    private Class<?> TargetClass;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8) {
            return;
        }
        in.markReaderIndex();
        int serializerCode = in.readInt();
        String serializerType = SerializerAdapter.getTypeByCode(serializerCode);
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        // 若当前可以获取到的字节数小于实际长度,则直接返回,直到当前可以获取到的字节数等于实际长度
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        // 读取完整的消息体字节数组
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        // 将字节数组反序列化为java对象(SerializerEngine参考序列化与反序列化章节)
        Object obj = SerializerAdapter.deserialize(data, TargetClass, serializerType);
        out.add(obj);
    }

    public NettyDecoderHandler(Class<?> targetClass) {
        TargetClass = targetClass;
    }
}
