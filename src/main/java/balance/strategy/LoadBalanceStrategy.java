package balance.strategy;

import zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

public interface LoadBalanceStrategy {
    ProviderRegisterMessage select(List<ProviderRegisterMessage> messages);
}
