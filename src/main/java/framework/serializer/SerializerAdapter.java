package framework.serializer;

import framework.serializer.HessianSerializer;
import framework.serializer.ProtoStuffSerializer;
import framework.serializer.Serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerAdapter {
    private static final Map<String, Serializer> SERIALIZER_MAP = new HashMap<>();

    public static final String PROTO_STUFF = "ProtoStuff";
    public static final String HESSIAN = "Hessian";
    private static final int HESSIAN_CODE =0;
    private static final int PROTO_STUFF_CODE =1 ;

    static {
        SERIALIZER_MAP.put(PROTO_STUFF, new ProtoStuffSerializer());
        SERIALIZER_MAP.put(HESSIAN, new HessianSerializer());
    }

    public static String getValidType(String serializerType) {
        if (HESSIAN.equalsIgnoreCase(serializerType)) {
            return HESSIAN;
        } else if (PROTO_STUFF.equalsIgnoreCase(serializerType)) {
            return PROTO_STUFF;
        } else {
            // 默认值
            return PROTO_STUFF;
        }
    }
//     序列化时,需要将采用的协议变成int存储在发生信息的头部

    public static int getCodeByType(String serializerType) {
        if (HESSIAN.equalsIgnoreCase(serializerType)) {
            return HESSIAN_CODE;
        } else if (PROTO_STUFF.equalsIgnoreCase(serializerType)) {
            return PROTO_STUFF_CODE;
        } else {
            return PROTO_STUFF_CODE;
        }
    }


//      反序列化时根据头部的int信息确定序列化协议
    public static String getTypeByCode(int serializerCode) {
        if (HESSIAN_CODE == serializerCode) {
            return HESSIAN;
        } else if (PROTO_STUFF_CODE == serializerCode) {
            return PROTO_STUFF;
        } else {
            return PROTO_STUFF;
        }
    }
//    序列化
    public static <T> byte[] serialize(T t, String serializerType) {
        // 忽略大小写,如果配置错误,选择默认序列化协议
        String type = getValidType(serializerType);
        return SERIALIZER_MAP.get(type).serialize(t);

    }
//    反序列化
    public static <T> T deserialize(byte[] data, Class<T> clazz, String serializerType) throws Exception {
        // 忽略大小写,如果配置错误,选择默认序列化协议
        String type = getValidType(serializerType);
        return SERIALIZER_MAP.get(type).deserialize(data, clazz);
    }
}
