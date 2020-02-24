package app;

import framework.Utils.ZkConfigHelper;
import framework.exception.ImportPropertyException;
import framework.factorybean.InvokerFactoryBean;
import framework.factorybean.ProviderFactoryBean;

import java.io.InputStream;
import java.util.Properties;

public class Server {
  public void setProperties(String path)
    {
        ProviderFactoryBean providerFactoryBean=new ProviderFactoryBean();
        Properties properties=new Properties();
        InputStream is = null;
        try {
            is = ZkConfigHelper.class.getResourceAsStream(path);
            if (null == is) {
                throw new IllegalStateException("service-properties can not found in the classpath.");
            }
            properties.load(is);
            providerFactoryBean.setId(Integer.parseInt(properties.getProperty("id")));
            providerFactoryBean.setTimeout(Integer.parseInt(properties.getProperty("timeout")));
            providerFactoryBean.setServicePath(properties.getProperty("interface"));
            providerFactoryBean.setAppName(properties.getProperty("appName"));
            providerFactoryBean.setRef(properties.getProperty("ref"));
            providerFactoryBean.setServerPort(Integer.parseInt(properties.getProperty("serverPort")));
            providerFactoryBean.setWorkerThreads(Integer.parseInt(properties.getProperty("workerThreads")));
            providerFactoryBean.afterPropertiesSet();
        } catch (Throwable t) {
            throw new ImportPropertyException("参数列表导入错误");
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
