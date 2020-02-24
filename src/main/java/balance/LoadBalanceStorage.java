package balance;

import balance.strategy.Impl.HashLoadBalanceStrategyImpl;
import balance.strategy.Impl.PollingLoadBalanceStrategyImpl;
import balance.strategy.Impl.WeightPollingLoadBalanceStrategyImpl;
import balance.strategy.Impl.WeightRandomLoadBalanceStrategyImpl;
import balance.strategy.LoadBalanceStrategy;
import balance.strategy.RandomLoadBalanceStrategyImpl;
import org.apache.commons.collections.map.HashedMap;
import zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;
import java.util.Map;
import java.util.PropertyPermission;

public class LoadBalanceStorage {
    private  static  final String RANDOM="Random";
    private  static  final String WEIGHTRANDOM="WeightRandom";
    private  static  final  String  POLLING="Polling";
    private  static  final String WEIGHTPOLLING="Weightpolling";
    private  static  final String HASH="hash";
    private static final Map<String, LoadBalanceStrategy> STRATEGY_MAP = new HashedMap();
    static {
        STRATEGY_MAP.put("Random", new RandomLoadBalanceStrategyImpl());
        STRATEGY_MAP.put("WeightRandom", new WeightRandomLoadBalanceStrategyImpl());
        STRATEGY_MAP.put("Polling", new PollingLoadBalanceStrategyImpl());
        STRATEGY_MAP.put("WeightPolling", new WeightPollingLoadBalanceStrategyImpl());
        STRATEGY_MAP.put("Hash", new HashLoadBalanceStrategyImpl());
    }

//    外部调用的select方法
    public  static ProviderRegisterMessage select(List<ProviderRegisterMessage>messages,String strategy)
    {
        strategy=getValidType(strategy);
        return STRATEGY_MAP.get(strategy).select(messages);
    }
//    对string进行标准化处理
    private   static String getValidType(String strategy) {
        if (RANDOM.equalsIgnoreCase(strategy)) {
            return RANDOM;
        } else if (WEIGHTPOLLING.equalsIgnoreCase(strategy)) {
            return WEIGHTPOLLING;
        } else if(POLLING.equalsIgnoreCase(strategy))
            return POLLING;
        else if(WEIGHTRANDOM.equalsIgnoreCase(strategy))
        {
            return WEIGHTRANDOM;
        }
        else  if(HASH.equalsIgnoreCase(strategy))
        {
            return HASH;
        }
        return RANDOM;

    }
}
