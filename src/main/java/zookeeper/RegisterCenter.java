package zookeeper;

import Utils.ZkConfigHelper;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public  abstract class RegisterCenter {


    public static final String ZK_SERVICE = ZkConfigHelper.getZkService();
    /**
     * ZK连接session超时时间
     */
   public static final int ZK_SESSION_TIME_OUT = ZkConfigHelper.getZkSessionTimeout();
    /**
     * ZK连接超时时间
     */
    public static final int ZK_CONNECTION_TIME_OUT =ZkConfigHelper.getZkConnectionTimeout();
    /**
     * 注册服务使用的根节点
     */
    public static final String ROOT_PATH = "/SmallDubbo";
    /**
     * 每个服务下表示服务提供者的父节点名
     */
    public static final String PROVIDER_TYPE = "provider";
    /**
     * 每个服务下表示服务使用者的父节点名
     */
    public static final String INVOKER_TYPE = "invoker";

//    zk服务器
    public static final ZkClient zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());


}
