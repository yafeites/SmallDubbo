package framework.balance.strategy.Impl;


import framework.balance.LoadBalanceStorage;
import framework.balance.strategy.Index;
import framework.balance.strategy.LoadBalanceStrategy;
import framework.exception.NoSuchServiceException;
import framework.zookeeper.InvokerRegisterCenter;
import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

/**
 * 轮询算法
 *
 */
public class PollingLoadBalanceStrategyImpl implements LoadBalanceStrategy {




    @Override
    public ProviderRegisterMessage select(String namespace) {
        List<ProviderRegisterMessage>messages=LoadBalanceStorage.getProviderMap(namespace);

        ProviderRegisterMessage registerMessage=null;
//        防止并发影响
        Index index=LoadBalanceStorage.getPoolingMap().get(namespace);
        synchronized (index)
        {
            int cnt=index.getValue();
            if (cnt >= messages.size()) {
                cnt = 0;
            }
            registerMessage = messages.get(cnt);
            index.setValue(cnt++);
        }
            return  registerMessage;

    }

}
