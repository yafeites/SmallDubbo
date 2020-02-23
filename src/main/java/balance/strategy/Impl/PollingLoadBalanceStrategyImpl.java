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
    private Lock lock = new ReentrantLock();

    @Override
    public ProviderRegisterMessage select(List<ProviderRegisterMessage> messages) {
        ProviderRegisterMessage registerMessage=null;
        try {
            // 尝试获取锁,10ms超时
            lock.tryLock(10, TimeUnit.MILLISECONDS);
            // 若计数大于服务提供者个数,将计数器归0
            if (index >= messages.size()) {
                index = 0;
            }
            registerMessage = messages.get(index);
            index++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
return  registerMessage;

    }

}
