package balance.strategy.Impl;

import balance.strategy.LoadBalanceStrategy;
import com.google.common.collect.Lists;

import exception.EmptyProviderListException;
import org.apache.commons.lang3.RandomUtils;
import zookeeper.registermessage.ProviderRegisterMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 软负载加权随机算法实现
 *
 * @author jacksu
 * @date 2018/8/8
 */
public class WeightRandomLoadBalanceStrategyImpl implements LoadBalanceStrategy {

    @Override
    public ProviderRegisterMessage select(List<ProviderRegisterMessage> messages) {
        // 根据加权创建服务列表索引:加权为3,则它的索引在这个数组中出现三次
        List<ProviderRegisterMessage> indexList = getIndexListByWeight(messages);
        int index = RandomUtils.nextInt(0, indexList.size());
        return indexList.get(index);
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
