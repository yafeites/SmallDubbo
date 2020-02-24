package netty;

import Utils.ZkConfigHelper;
import balance.LoadBalanceStorage;
import call.InvokerCallableService;
import exception.NoSuchServiceException;
import io.netty.util.concurrent.DefaultThreadFactory;
import message.RequestMessage;
import message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zookeeper.InvokerRegisterCenter;
import zookeeper.ProviderRegisterCenter;
import zookeeper.registermessage.ProviderRegisterMessage;

import java.lang.reflect.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class ClientProxyFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientProxyFactory.class);
//    单例模式
    private  static  final ClientProxyFactory clientProxyFactory=new ClientProxyFactory();

    private static final InvokerRegisterCenter invokerRegisterCenter = InvokerRegisterCenter.getInstance();

    private static volatile  ExecutorService fixedThreadPool;
    public static <T> T getProxyInstance(String appName, final Class<T> serviceInterface, int consumeTimeout, final String loadBalanceStrategy)
    {
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{serviceInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//                拿到需要调用的服务
                String nameSpace = appName + "/" + serviceInterface.getName();
                List<ProviderRegisterMessage> list=invokerRegisterCenter.getProviderMap().get(nameSpace);
                if(list==null||list.size()==0)
                {
                    throw new NoSuchServiceException("没有可用节点");
                }
             ProviderRegisterMessage providerRegisterMessage =LoadBalanceStorage.select(list,loadBalanceStrategy);
//                构造请求message
                RequestMessage request=new RequestMessage();
                request.setServicePath(serviceInterface.getName());
                request.setTimeout(consumeTimeout);
                request.setTraceId(UUID.randomUUID().toString());

                request.setRefId(providerRegisterMessage.getRefId());
                request.setWorkerThread(providerRegisterMessage.getWorkerThread());
                request.setMethodName(method.getName());
                if (null != args && args.length > 0) {
                    // 设置方法参数和类型(类型用于反射得到Method对象)
                    request.setParameters(args);
                    request.setParameterTypes(new String[args.length]);
                    Type[] types = method.getGenericParameterTypes();
                        for (int i = 0; i < args.length; i++) {
                        // 所以当args参数是泛型时,需要寻找它的顶级type的类名
                        if (types[i] instanceof ParameterizedType) {
                            request.getParameterTypes()[i] = ((ParameterizedType) types[i]).getRawType().getTypeName();
                        } else {
                            request.getParameterTypes()[i] = types[i].getTypeName();
                        }
                    }
                }
                Future<ResponseMessage> responseMessage = null;
                if(fixedThreadPool==null)
                {
                    synchronized (this)
                    {
                        if(fixedThreadPool==null)
                        {
                            int corePoolSize= ZkConfigHelper.getThreadWorkerNumber();
                            fixedThreadPool= new ThreadPoolExecutor(corePoolSize/2, corePoolSize, 0, TimeUnit.MILLISECONDS,
                                    new ArrayBlockingQueue<>(100));
                        }
                    }
                }
                String serverIp = providerRegisterMessage.getServerIp();
                int serverPort = providerRegisterMessage.getServerPort();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(serverIp, serverPort);
                Future<ResponseMessage>result=null;
                result=fixedThreadPool.submit(new InvokerCallableService(inetSocketAddress,request));
                try
                {
                    ResponseMessage response=result.get(consumeTimeout,TimeUnit.SECONDS);
                    return response.getReturnValue();
                }
               catch ( InterruptedException e)
               {
                   e.printStackTrace();
               }
                return  null;
                }
            });
        LOGGER.info("创建代理对象成功[{}]",serviceInterface.getName());
        return (T) proxy;
        }

    }

