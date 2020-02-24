package balance.strategy.Impl;


import balance.strategy.LoadBalanceStrategy;
import zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 软负载轮询算法实现
 *
 * @author jacksu
 * @date 2018/8/8
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