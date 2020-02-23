package balance.strategy;

import zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

public class RandomLoadBalanceStrategyImpl implements LoadBalanceStrategy {

    @Override
    public ProviderRegisterMessage select(List<ProviderRegisterMessage> messages) {
        return null;
    }
}
