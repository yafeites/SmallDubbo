package app;

import framework.Utils.ZkConfigHelper;
import framework.exception.ImportPropertyException;
import framework.factorybean.InvokerFactoryBean;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
  public   Object setProperties(String path)  {

        InvokerFactoryBean invokerFactoryBean=new InvokerFactoryBean();
        Properties properties=new Properties();
        InputStream is = null;
        try {
            is = ZkConfigHelper.class.getResourceAsStream(path);
            if (null == is) {
                throw new IllegalStateException("invoker-properties can not found in the classpath.");
            }

                properties.load(is);
                invokerFactoryBean.setId(Integer.parseInt(properties.getProperty("id")));
                invokerFactoryBean.setTimeout(Integer.parseInt(properties.getProperty("timeout")));
                invokerFactoryBean.setTargetInterface(Class.forName(properties.getProperty("interface")));
                invokerFactoryBean.setClusterStrategy(properties.getProperty("clusterStrategy"));
                invokerFactoryBean.setAppName(properties.getProperty("appName"));
                invokerFactoryBean.afterPropertiesSet();
                return invokerFactoryBean.getObject();

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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
