package framework.balance.strategy.Impl;


import framework.balance.strategy.LoadBalanceStrategy;
import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;

/**
 * 轮询算法
 *
 */
public class PollingLoadBalanceStrategyImpl implements LoadBalanceStrategy {

    /**
     * 计数器
     */
    private int index = 0;
    /**
     * 计数器锁
     */
    private Object lock=new Object();

    @Override
    public ProviderRegisterMessage select(List<ProviderRegisterMessage> messages) {
        ProviderRegisterMessage registerMessage=null;
//        防止并发影响
        synchronized (lock)
        {
            if (index >= messages.size()) {
                index = 0;
            }
            registerMessage = messages.get(index);
            index++;
        }
            return  registerMessage;

    }

}
