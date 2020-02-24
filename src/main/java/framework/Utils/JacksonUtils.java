package framework.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JacksonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);

//    jackson对象
    private static ObjectMapper MAPPER = new ObjectMapper();
//     将对象转成json
    public static String objectToJson(Object obj) {
        try {
            String str = MAPPER.writeValueAsString(obj);
            return str;
        } catch (JsonProcessingException e) {
            LOGGER.error("objectToJson error");
            e.printStackTrace();
        }
        return null;
    }

//   将json转成字符串
    public static <T> T jsonToObject(String json, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(json, beanType);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("jsonToObject error");
        }
        return null;
    }
}
