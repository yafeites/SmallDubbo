package framework.balance.strategy.Impl;

import framework.balance.LoadBalanceStorage;
import framework.balance.strategy.Index;
import framework.balance.strategy.LoadBalanceStrategy;
import framework.exception.EmptyProviderListException;
import framework.exception.NoSuchServiceException;
import framework.zookeeper.InvokerRegisterCenter;
import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 权重轮询
 *
 */
public class WeightPollingLoadBalanceStrategyImpl implements LoadBalanceStrategy {
    @Override
    public ProviderRegisterMessage select(String namespace) {

        List<ProviderRegisterMessage>messages=LoadBalanceStorage.getProviderMap(namespace);
        ProviderRegisterMessage registerMessage = null;
                List<ProviderRegisterMessage> indexList = getIndexListByWeight(messages);
        Index index=LoadBalanceStorage.getWEIGHTPOOLINGMAP().get(namespace);
        synchronized (index)
        {
            int cnt=index.getValue();
                // 若计数大于服务提供者个数,将计数器归0
                if (cnt >= indexList.size()) {
                    cnt = 0;
                }
                registerMessage = indexList.get(cnt);
                index.setValue(cnt++);
            }
            // 根据加权创建服务列表索引:加权为3,则它的索引在这个数组中出现三次
        return  registerMessage;
    }
    public  List<ProviderRegisterMessage> getIndexListByWeight(List<ProviderRegisterMessage> providerServices) {
        if (null == providerServices | providerServices.size() == 0) {
            throw  new EmptyProviderListException("无服务提供者信息");
        }
        ArrayList<ProviderRegisterMessage> list = new ArrayList();
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
