package framework.balance.strategy;

import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

public interface LoadBalanceStrategy {
    ProviderRegisterMessage select(String namespace);
}
