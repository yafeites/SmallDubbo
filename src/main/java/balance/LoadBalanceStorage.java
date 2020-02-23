package balance;

import balance.strategy.Impl.HashLoadBalanceStrategyImpl;
import balance.strategy.Impl.PollingLoadBalanceStrategyImpl;
import balance.strategy.Impl.WeightPollingLoadBalanceStrategyImpl;
import balance.strategy.Impl.WeightRandomLoadBalanceStrategyImpl;
import balance.strategy.LoadBalanceStrategy;
import balance.strategy.RandomLoadBalanceStrategyImpl;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

public class LoadBalanceStorage {
    private static final Map<String, LoadBalanceStrategy> STRATEGY_MAP = new HashedMap();
    static {
        STRATEGY_MAP.put("Random", new RandomLoadBalanceStrategyImpl());
        STRATEGY_MAP.put("WeightRandom", new WeightRandomLoadBalanceStrategyImpl());
        STRATEGY_MAP.put("Polling", new PollingLoadBalanceStrategyImpl());
        STRATEGY_MAP.put("WeightPolling", new WeightPollingLoadBalanceStrategyImpl());
        STRATEGY_MAP.put("Hash", new HashLoadBalanceStrategyImpl());
    }
}
