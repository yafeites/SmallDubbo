package framework.zookeeper.registermessage;


public class ProviderRegisterMessage {
//    服务app名称
    private String appName;
//    服务接口全限定名
    private String servicePath;
//    对应的真正服务接口的实现类bean id 名称
    private String refId;
//    服务主机名
    private String serverIp;
//    服务端口
    private int serverPort;
//    超时时间
    private long timeout;
//    当前最大接受调用数量
    private int workerThread;
//    用于负载均衡权重计算
    private int weight;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getWorkerThread() {
        return workerThread;
    }

    public void setWorkerThread(int workerThread) {
        this.workerThread = workerThread;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
