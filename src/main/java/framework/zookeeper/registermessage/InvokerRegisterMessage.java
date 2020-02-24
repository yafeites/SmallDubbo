package framework.zookeeper.registermessage;

import java.util.UUID;

public class InvokerRegisterMessage {
//    服务app名称
    private String appName;

    //接口全限定名
    private String servicePath;

    //本机信息

    private static String invokerMachine = UUID.randomUUID().toString();

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

    public static String getInvokerMachine() {
        return invokerMachine;
    }

    public static void setInvokerMachine(String invokerMachine) {
        InvokerRegisterMessage.invokerMachine = invokerMachine;
    }
}
