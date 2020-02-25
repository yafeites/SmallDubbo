package framework.factorybean;


import framework.netty.ClientProxyFactory;
import framework.netty.NettyChannelPoolFactory;
import framework.zookeeper.InvokerRegisterCenter;
import framework.zookeeper.registermessage.InvokerRegisterMessage;
import framework.zookeeper.registermessage.ProviderRegisterMessage;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class InvokerFactoryBean implements FactoryBean, InitializingBean {
    private static Set<InetSocketAddress> socketAddressSet = new HashSet<>();

    /**
     * ChannelPool工厂
     */
    private static NettyChannelPoolFactory nettyChannelPoolFactory = NettyChannelPoolFactory.getInstance();

    /**
     * 注册中心
     */
    private static InvokerRegisterCenter registerCenter = InvokerRegisterCenter.getInstance();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private  int id;
    /**
     * 服务接口
     */
    private Class<?> targetInterface;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 服务所属应用名
     */
    private String appName;
    /**
     * 负载均衡策略
     */
    private String clusterStrategy ;

    /**
     * invoker的初始化: 获取引用服务的远程地址 / 新的远程地址会生成一定数量的channel到channelpool中
     */
    @Override
    public void afterPropertiesSet() {
        // 将标签内容注册到zk中,同时获取标签内容的服务地址到本地
        InvokerRegisterMessage invoker = new InvokerRegisterMessage();
        invoker.setServicePath(targetInterface.getName());
        invoker.setAppName(appName);
        // 本机所有invoker的machineID是一样的
        // 根据标签内容从注册中心获取的地址
        List<ProviderRegisterMessage> providerRegisterMessages = registerCenter.registerInvoker(invoker);
        // 提前为不同的主机地址创建ChannelPool
        for (ProviderRegisterMessage provider : providerRegisterMessages) {
            InetSocketAddress socketAddress = new InetSocketAddress(provider.getServerIp(), provider.getServerPort());
            boolean firstAdd = socketAddressSet.add(socketAddress);
            if (firstAdd) {
                nettyChannelPoolFactory.registerChannelQueueToMap(socketAddress);
            }
        }
    }

    /**
     *
     *
     * @return 引用服务接口的代理对象
     */
    @Override
    public Object getObject() {
        return ClientProxyFactory.getProxyInstance(appName, targetInterface, timeout, clusterStrategy);
    }

    /**
     * 声明接口代理对象的类型
     *
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    /**
     * 声明是否单例
     *
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<?> getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getClusterStrategy() {
        return clusterStrategy;
    }

    public void setClusterStrategy(String clusterStrategy) {
        this.clusterStrategy = clusterStrategy;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
