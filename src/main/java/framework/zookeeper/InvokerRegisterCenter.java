package framework.zookeeper;

import framework.Utils.JacksonUtils;
import org.I0Itec.zkclient.IZkChildListener;
import framework.zookeeper.registermessage.InvokerRegisterMessage;
import framework.zookeeper.registermessage.ProviderRegisterMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvokerRegisterCenter extends RegisterCenter{

    private static final Logger LOGGER = LoggerFactory.getLogger(InvokerRegisterCenter.class);

    public  Map<String, List<ProviderRegisterMessage>> getProviderMap() {
        return PROVIDER_MAP;
    }

    private static final Map<String, List<ProviderRegisterMessage>> PROVIDER_MAP = new ConcurrentHashMap<>(new HashMap<>());

    private   static  final  InvokerRegisterCenter instance= new InvokerRegisterCenter();

    public  static InvokerRegisterCenter getInstance()
    {
        return instance;
    }



    public List<ProviderRegisterMessage> registerInvoker(InvokerRegisterMessage invoker) {
        long startTime = System.currentTimeMillis();
        List<ProviderRegisterMessage> providerRegisterMessages = null;
        // 创建服务接口的命名空间
        String nameSpace = invoker.getAppName() + "/" + invoker.getServicePath();

            // 创建invoker命名空间(持久节点)
            String invokerPath = ROOT_PATH + "/" + nameSpace + "/" + INVOKER_TYPE;
            boolean exist = zkClient.exists(invokerPath);
            if (!exist) {
                zkClient.createPersistent(invokerPath, true);
            }
            // 创建invoker注册信息节点(临时节点)
            String invokerMsgNode = invokerPath + "/" + JacksonUtils.objectToJson(invoker);
            // 创建临时节点(临时节点才能自动监听)
            exist = zkClient.exists(invokerMsgNode);
            if (!exist) {
                zkClient.createEphemeral(invokerMsgNode);
            }
            // 获取服务节点
            String servicePath = ROOT_PATH + "/" + nameSpace + "/" + PROVIDER_TYPE;
            // 本地缓存没有这个接口key,表明该接口是第一次添加引用声明,此时需要为该接口添加一个监听器
            // 不过接口引用声明一般也只会出现一次
            if (null == PROVIDER_MAP.get(nameSpace)) {
                // 为每个服务注册监听器,实现服务自动发现
                zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                    @Override
                    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                        // 服务接口的全限定名
                        if (null == currentChilds || currentChilds.size() == 0) {
                            PROVIDER_MAP.remove(nameSpace);
                            LOGGER.warn("[{}]节点发生变化,该节点下已无可用服务", parentPath);
                            return;
                        }
                        // 监听到变化后servicePath节点下的所有临时节点值是currentChilds
                        List<ProviderRegisterMessage> newProviderList =new ArrayList<>();
                        for (String each : currentChilds) {
                            newProviderList.add(JacksonUtils.jsonToObject(each, ProviderRegisterMessage.class));
                        }
                        // 更新本地缓存的服务信息
                        PROVIDER_MAP.put(nameSpace, newProviderList);
                        LOGGER.info("[{}]节点发生变化,重新加载该节点下的服务信息如下", parentPath);
                        System.out.println(newProviderList);
                    }
                });
            }
            // 获取服务节点下所有临时节点(服务注册信息列表)
            List<String> providerStrings = zkClient.getChildren(servicePath);
            // 根据注册信息字符串还原注册信息
            providerRegisterMessages = new ArrayList<>();
            for (String each : providerStrings) {
                providerRegisterMessages.add(JacksonUtils.jsonToObject(each, ProviderRegisterMessage.class));
            }
            // 将注册信息缓存到本地
            PROVIDER_MAP.put(nameSpace, providerRegisterMessages);

        long times = System.currentTimeMillis() - startTime;
        LOGGER.info("获取服务地址耗时{}ms:[{}]", times, nameSpace);
        return providerRegisterMessages;
    }
}
