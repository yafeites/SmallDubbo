package framework.factorybean;

import framework.Utils.IPHelper;
import framework.netty.NettyServer;
import framework.zookeeper.ProviderRegisterCenter;
import framework.zookeeper.registermessage.ProviderRegisterMessage;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

public class ProviderFactoryBean implements FactoryBean, InitializingBean {

    private static final Map<Integer, NettyServer> NETTY_SERVER_MAP = new HashMap<>();

    /**
     * 注册中心
     */
    private static ProviderRegisterCenter providercenter = ProviderRegisterCenter.getInstance();


    /**
     * 接口所在应用名
     */
    private String appName;
    /**
     * 服务接口
     */
    private String servicePath;
    /**
     * 服务接口实现类对象(通过其获取实现类全限定名)
     */
    private String ref;
    /**
     * 服务端口
     */
    private Integer serverPort;


    /**
     * 服务提供者权重,范围为[1-100]
     */
    private int weight = 1;
    /**
     * 服务端限流信号量大小
     */
    private int workerThreads = 10;

    private  int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 服务实现类对象已在配置文件中声明了bean标签
     */
    @Override
    public Object getObject() {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return Object.class;
    }

    /**
     * 声明是否单例
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        // 组装标签信息
        ProviderRegisterMessage provider = new ProviderRegisterMessage();
        provider.setAppName(appName);
        provider.setServicePath(servicePath);
        provider.setRefId(ref);
        // 获取本机ip地址
        provider.setServerIp(IPHelper.localIp());
        provider.setServerPort(serverPort);
        // 以下都有默认值,如果标签内容有就是标签值,但是都要配置
        provider.setWorkerThread(workerThreads);
        provider.setWeight(weight);
        // 注册服务到zk
        providercenter.registerProvider(provider);
        NettyServer nettyServer = NETTY_SERVER_MAP.get(serverPort);
        // 如果缓存中没有这个端口,就开启服务
        if (null == nettyServer) {
            // 使用新的NettyServer开启服务,需要记录在本地缓存
            synchronized (ProviderFactoryBean.class) {
                // 可能会有多个服务对象绑定在同一端口，保证它们只能创建一个nettyServer
                if (null == NETTY_SERVER_MAP.get(serverPort)) {
                    // 双重校验
                    nettyServer = new NettyServer();
                    nettyServer.start(serverPort);
                    NETTY_SERVER_MAP.put(serverPort, nettyServer);
                }
            }
        }
    }

    public Object getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public static Map<Integer, NettyServer> getNettyServerMap() {
        return NETTY_SERVER_MAP;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
