package framework.balance.strategy.Impl;

import framework.balance.strategy.LoadBalanceStrategy;
import org.apache.commons.lang3.RandomUtils;
import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

/**
 *随机算法
 *
 */
public class RandomLoadBalanceStrategyImpl implements LoadBalanceStrategy {

    @Override
    public ProviderRegisterMessage select(List<ProviderRegisterMessage> messages) {
        int index = RandomUtils.nextInt(0, messages.size());
        return messages.get(index);
    }
}
