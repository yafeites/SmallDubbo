package framework.balance.strategy.Impl;


import framework.Utils.IPHelper;
import framework.balance.LoadBalanceStorage;
import framework.balance.strategy.LoadBalanceStrategy;
import framework.exception.NoSuchServiceException;
import framework.zookeeper.InvokerRegisterCenter;
import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

/**
 * hash轮转
 *
 */
public class HashLoadBalanceStrategyImpl implements LoadBalanceStrategy {

    @Override
    public ProviderRegisterMessage select(String namespace) {
        List<ProviderRegisterMessage>messages= LoadBalanceStorage.getProviderMap(namespace);
        // 获取调用方ip
        String localIP = IPHelper.localIp();
        // 获取源地址对应的hashcode
        int hashCode = localIP.hashCode();
        // 获取服务列表大小
        int size = messages.size();
        return messages.get(hashCode % size);
    }
}
