package framework.balance.strategy.Impl;


import framework.Utils.IPHelper;
import framework.balance.strategy.LoadBalanceStrategy;
import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

/**
 * 软负载hash算法实现
 *
 * @author jacksu
 * @date 2018/8/8
 */
public class HashLoadBalanceStrategyImpl implements LoadBalanceStrategy {

    @Override
    public ProviderRegisterMessage select(List<ProviderRegisterMessage> messages) {
        // 获取调用方ip
        String localIP = IPHelper.localIp();
        // 获取源地址对应的hashcode
        int hashCode = localIP.hashCode();
        // 获取服务列表大小
        int size = messages.size();
        return messages.get(hashCode % size);
    }
}
