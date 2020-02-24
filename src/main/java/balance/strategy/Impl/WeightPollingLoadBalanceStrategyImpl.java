package balance.strategy.Impl;

import balance.strategy.LoadBalanceStrategy;
import com.google.common.collect.Lists;
import exception.EmptyProviderListException;
import zookeeper.registermessage.ProviderRegisterMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 软负载加权轮询算法实现
 *
 * @author jacksu
 * @date 2018/8/8
 */
public class WeightPollingLoadBalanceStrategyImpl implements LoadBalanceStrategy {

//   计数器
    private int index = 0;
//    对象锁
    private  Object lock=new Object();

    @Override
    public ProviderRegisterMessage select(List<ProviderRegisterMessage> messages) {
        ProviderRegisterMessage registerMessage = null;


                List<ProviderRegisterMessage> indexList = getIndexListByWeight(messages);
        synchronized (lock)
        {
                // 若计数大于服务提供者个数,将计数器归0
                if (index >= indexList.size()) {
                    index = 0;
                }
                registerMessage = indexList.get(index);
                index++;
            }
            // 根据加权创建服务列表索引:加权为3,则它的索引在这个数组中出现三次
        return  registerMessage;
    }
    public  List<ProviderRegisterMessage> getIndexListByWeight(List<ProviderRegisterMessage> providerServices) {
        if (null == providerServices | providerServices.size() == 0) {
            throw  new EmptyProviderListException("无服务提供者信息");
        }
        ArrayList<ProviderRegisterMessage> list = Lists.newArrayList();
        int index = 0;
        for (ProviderRegisterMessage each : providerServices) {
            int weight = each.getWeight();
            while (weight-- > 0) {
                list.add(each);
            }
            index++;
        }
        return list;
    }

}
