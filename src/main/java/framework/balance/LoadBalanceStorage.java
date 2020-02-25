package framework.balance;

import framework.balance.strategy.Impl.*;
import framework.balance.strategy.Index;
import framework.balance.strategy.LoadBalanceStrategy;
import framework.exception.NoSuchServiceException;
import framework.zookeeper.InvokerRegisterCenter;
import org.apache.commons.collections.map.HashedMap;
import framework.zookeeper.registermessage.ProviderRegisterMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoadBalanceStorage {
    public static Map<String, Index> getPoolingMap() {
        return POOLINGMAP;
    }

    public  static void setPoolingMap(Map<String, Index> POOLINGMAP) {
        POOLINGMAP = POOLINGMAP;
    }

    public static Map<String, Index> getWEIGHTPOOLINGMAP() {
        return WEIGHTPOOLINGMAP;
    }

    public static void setWEIGHTPOOLINGMAP(Map<String, Index> WEIGHTPOOLINGMAP) {
        LoadBalanceStorage.WEIGHTPOOLINGMAP = WEIGHTPOOLINGMAP;
    }

    private  static  Map<String,Index>WEIGHTPOOLINGMAP=new ConcurrentHashMap<>();
    private   static  Map<String ,Index> POOLINGMAP=new ConcurrentHashMap<>();
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
    public  static ProviderRegisterMessage select(String strategy,String namespace)
    {
        strategy=getValidType(strategy);
        return STRATEGY_MAP.get(strategy).select(namespace);
    }
    public  static  List<ProviderRegisterMessage> getProviderMap(String namespace)
    {
        List<ProviderRegisterMessage> messages= InvokerRegisterCenter.getProviderMap().get(namespace);
        if(messages==null||messages.size()==0)
        {
            throw new NoSuchServiceException("没有可用节点");
        }
        return messages;
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
