package framework.balance.strategy.Impl;

import framework.balance.LoadBalanceStorage;
import framework.balance.strategy.LoadBalanceStrategy;
import framework.exception.NoSuchServiceException;
import framework.zookeeper.InvokerRegisterCenter;
import org.apache.commons.lang3.RandomUtils;
import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

/**
 *随机算法
 *
 */
public class RandomLoadBalanceStrategyImpl implements LoadBalanceStrategy {

    @Override
    public ProviderRegisterMessage select(String namespace) {

        List<ProviderRegisterMessage>messages= LoadBalanceStorage.getProviderMap(namespace);
        int index = RandomUtils.nextInt(0, messages.size());
        return messages.get(index);
    }
}
