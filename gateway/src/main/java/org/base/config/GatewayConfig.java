package org.base.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.dto.ApiDTO;
import org.base.dto.TopicMapper;
import org.base.exception.SystemException;
import org.base.ultilities.Constants;
import org.base.ultilities.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@Component
public class GatewayConfig {

    public String CONFIG_DIR = System.getProperty("user.dir") + File.separator  +
                                    "gateway/config" + File.separator;
    public static final String API_ROOT_PATH = "/api/";

    public static Set<String> SERVICE_LIST = new HashSet<>();
    public static final Map<String, ApiDTO> MAPPING_SERVICE_PATH = new HashMap<>();

    public static final Map<String, TopicMapper> MAPPING_SERVICE_TOPIC = new HashMap<>();

    @PostConstruct
    private void loadConfig() {
        try {
            log.info("======= Loading Services... =======");
            SERVICE_LIST.add(Constants.SERVICE.EXAM);
            SERVICE_LIST.add(Constants.SERVICE.CONVERSATION);
            SERVICE_LIST.add(Constants.SERVICE.BLOG);
            SERVICE_LIST.add(Constants.SERVICE.USER);
            SERVICE_LIST.add(Constants.SERVICE.DOCHUB);
            SERVICE_LIST.add(Constants.SERVICE.COURSE);

            log.info("======= Loading GatewayConfig config... =======");
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream(CONFIG_DIR + "gateway.properties"), "UTF-8"));
            Enumeration<?> e = properties.propertyNames();

            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);

                System.out.println("+ key: " + key + " => value: " + value);
                if(key.contains("topic")) {
                    String service = key.split("\\.")[1];
                    String topicTo, topicFrom;
                    if(SERVICE_LIST.contains(service)) {
                        TopicMapper mapper = MAPPING_SERVICE_TOPIC.getOrDefault(service, new TopicMapper());
                        if(key.contains("from")) {
                            mapper.setFrom(value);
                        } else {
                            mapper.setTo(value);
                        }
                        MAPPING_SERVICE_TOPIC.put(service, mapper);
                    }
                }
            }
            log.info("Mapping service topic {}", MAPPING_SERVICE_TOPIC);
            System.out.println("=== Load GatewayConfig config successfull!!! ===");
        } catch (IOException ex) {
            Logger.getLogger(GatewayConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadAPIJson(String jsonFile) {
        try {
            FileReader reader = new FileReader(CONFIG_DIR + jsonFile + ".json");
            ObjectMapper mapper = new ObjectMapper();
            List<ApiDTO> apis = mapper.readValue(reader, new TypeReference<List<ApiDTO>>() {});
            if (!StringUtil.isListEmpty(apis)) {
                apis.forEach((tmp) -> {
                    MAPPING_SERVICE_PATH.put(tmp.getPath() + Constants.TEMPLE_SPLIT + tmp.getMethod(), tmp);
                });
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new SystemException("File gateway.properties bị lỗi!!");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new SystemException("Lấy data từ file gateway.properties bị lỗi!!");
        }
    }

    public static void main(String[] args) {
        new GatewayConfig().loadConfig();
    }


    public static String validate(String requestPath, String service, String method) {
        if (StringUtil.isNullOrEmpty(requestPath)) {
            return "Path request không được trống";
        }
        if(StringUtil.isNullOrEmpty(service) || !GatewayConfig.SERVICE_LIST.contains(service)){
            return "Service request không tồn tại";
        }
        requestPath = requestPath.contains(GatewayConfig.API_ROOT_PATH)
                ? requestPath.replace(GatewayConfig.API_ROOT_PATH, "/") : requestPath;
        int index = requestPath.lastIndexOf("/");
        if(index != -1){
            String lastStr = requestPath.substring(index + 1);
            if(StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)){
                requestPath = requestPath.substring(0, index);
            }
        }

        //check them request path

        return null;
    }
}
