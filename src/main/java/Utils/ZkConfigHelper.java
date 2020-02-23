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

    public static String getZkService() {
        return zkService;
    }

    public static void setZkService(String zkService) {
        ZkConfigHelper.zkService = zkService;
    }

    public static String getAppName4Server() {
        return appName4Server;
    }

    public static void setAppName4Server(String appName4Server) {
        ZkConfigHelper.appName4Server = appName4Server;
    }

    public static String getAppName4Client() {
        return appName4Client;
    }

    public static void setAppName4Client(String appName4Client) {
        ZkConfigHelper.appName4Client = appName4Client;
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

    public static String getDefaultClusterStrategy() {
        return defaultClusterStrategy;
    }

    public static void setDefaultClusterStrategy(String defaultClusterStrategy) {
        ZkConfigHelper.defaultClusterStrategy = defaultClusterStrategy;
    }

    public static String getSerializer() {
        return Serializer;
    }

    public static void setSerializer(String serializer) {
        Serializer = serializer;
    }

    /*必须要显示声明的配置项(没有默认值)*/
    // ZK服务地址
    private static String zkService = "";
    /*标签优先级更高,如果标签声明了,则不需要再显示声明,标签没声明的使用配置项*/
    // 服务方注册时候的应用名
    private static String appName4Server = "";
    // 使用方引用时候的应用名
    private static String appName4Client = "";

    /*有默认值的配置项*/
    // ZK session超时时间
    private static int zkSessionTimeout;
    // ZK connection超时时间
    private static int zkConnectionTimeout;
    // 每个服务端提供者的Netty的连接数
//    private static int channelPoolSize;
//    // 客户端调用rpc服务线程池的线程数量
//    private static int threadWorkerNumber;
//    // 默认的负载均衡策略
    private static String defaultClusterStrategy;
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
            appName4Server = properties.getProperty("server.app.name");
            appName4Client = properties.getProperty("client.app.name");
            zkSessionTimeout = Integer.parseInt(properties.getProperty("zookeeper.session.timeout", "500"));
            zkConnectionTimeout = Integer.parseInt(properties.getProperty("zookeeper.connection.timeout", "500"));
            defaultClusterStrategy = properties.getProperty("client.clusterStrategy.default", "random");
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
}
