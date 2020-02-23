package zookeeper;


import Utils.JacksonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zookeeper.message.InvokerRegisterMessage;
import zookeeper.message.ProviderRegisterMessage;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ProviderRegisterCenter extends RegisterCenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderRegisterCenter.class);
    private static final Map<String, List<InvokerRegisterMessage>> INVOKER_MAP = new ConcurrentHashMap<>();
    private static Set<String> invokerNodeListenerSet =new CopyOnWriteArraySet<>();

    public void registerProvider(ProviderRegisterMessage provider) {
        long startTime = System.currentTimeMillis();
        // 创建服务接口的命名空间
        String nameSpace = provider.getAppName() + "/" + provider.getServicePath();
        synchronized (RegisterCenter.class) {
            // ROOT_PATH/应用名/接口全限定名/provider(持久节点)
            String providerPath = ROOT_PATH + "/" + nameSpace + "/" + PROVIDER_TYPE;
            // 创建服务接口/provider的永久节点
            if (!zkClient.exists(providerPath)) {
                zkClient.createPersistent(providerPath, true);
            }
            // 注册服务信息(临时节点)
            String serviceMsgNode = providerPath + "/" + JacksonUtils.objectToJson(provider);
            // 创建临时节点(临时节点才能自动监听)
            if (!zkClient.exists(serviceMsgNode)) {
                zkClient.createEphemeral(serviceMsgNode);
            }
            // 创建服务接口/invoker的永久节点
            String invokerPath = ROOT_PATH + "/" + nameSpace + "/" + INVOKER_TYPE;
            if (!zkClient.exists(invokerPath)) {
                zkClient.createPersistent(invokerPath);
            }
            boolean firstAdd = invokerNodeListenerSet.add(invokerPath);
            // 为服务/invoker节点注册监听器,便于服务提供方获取该服务下的使用者(避免重复监听)
            if (firstAdd) {
                zkClient.subscribeChildChanges(invokerPath, new IZkChildListener() {
                    @Override
                    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                        // 服务接口的全限定名
                        if (null == currentChilds || currentChilds.size() == 0) {
                            INVOKER_MAP.remove(nameSpace);
                            LOGGER.warn("[{}]节点发生变化,该服务节点下已无调用者", parentPath);
                            return;
                        }
                        // 监听到变化后invokerPath节点下的所有临时节点值是currentChilds
                        // 根据字符串节点值,还原Invoker
                        List<InvokerRegisterMessage> newInvokerList = Lists.newArrayList();
                        for (String each : currentChilds) {
                            newInvokerList.add(JacksonUtils.jsonToObject(each, InvokerRegisterMessage.class));
                        }
                        // 更新invoker缓存
                        INVOKER_MAP.put(nameSpace, newInvokerList);
                        LOGGER.info("[{}]节点发生变化,重新加载该节点下的invoker信息如下", parentPath);
                        System.out.println(newInvokerList);
                    }
                });
            }
        }
        long times = System.currentTimeMillis() - startTime;
        LOGGER.info("注册服务耗时{}ms [服务路径:/zookeeper/{}/{}]", times, nameSpace, provider.getRefname());
    }
}
