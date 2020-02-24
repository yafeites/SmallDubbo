package Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ZkConfigHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkConfigHelper.class);

    private static final String PROPERTY_CLASSPATH = "/zk.properties";
    private static final Properties properties = new Properties();

    public static String getPropertyClasspath() {
        return PROPERTY_CLASSPATH;
    }

    public static Properties getProperties() {
        return properties;
    }



    public static int getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public static void setZkSessionTimeout(int zkSessionTimeout) {
        ZkConfigHelper.zkSessionTimeout = zkSessionTimeout;
    }

    public static int getZkConnectionTimeout() {
        return zkConnectionTimeout;
    }

    public static void setZkConnectionTimeout(int zkConnectionTimeout) {
        ZkConfigHelper.zkConnectionTimeout = zkConnectionTimeout;
    }




    public static String getSerializer() {
        return Serializer;
    }

    public static void setSerializer(String serializer) {
        Serializer = serializer;
    }

    private static String zkService = "";
    // ZK session超时时间
    private static int zkSessionTimeout;
    // ZK connection超时时间
    private static int zkConnectionTimeout;
//     每个服务端提供者的Netty的连接数
    private static int channelPoolSize;
    // 客户端调用rpc服务线程池的线程数量
    private static int threadWorkerNumber;

//   采用的序列化协议
    private static String Serializer;



    /**
     * 初始化
     */
    static {
        InputStream is = null;
        try {
            is = ZkConfigHelper.class.getResourceAsStream(PROPERTY_CLASSPATH);
            if (null == is) {
                throw new IllegalStateException("Zk.properties can not found in the classpath.");
            }
            properties.load(is);
            zkService = properties.getProperty("zookeeper.address");
            zkSessionTimeout = Integer.parseInt(properties.getProperty("zookeeper.session.timeout", "500"));
            zkConnectionTimeout = Integer.parseInt(properties.getProperty("zookeeper.connection.timeout", "500"));
            Serializer = properties.getProperty("server.serializer", "Default");


        } catch (Throwable t) {
            LOGGER.warn("load Zk.properties file failed.", t);
            throw new RuntimeException(t);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getZkService() {
        return zkService;
    }

    public static void setZkService(String zkService) {
        ZkConfigHelper.zkService = zkService;
    }

    public static int getChannelPoolSize() {
        return channelPoolSize;
    }

    public static void setChannelPoolSize(int channelPoolSize) {
        ZkConfigHelper.channelPoolSize = channelPoolSize;
    }

    public static int getThreadWorkerNumber() {
        return threadWorkerNumber;
    }

    public static void setThreadWorkerNumber(int threadWorkerNumber) {
        ZkConfigHelper.threadWorkerNumber = threadWorkerNumber;
    }
}
