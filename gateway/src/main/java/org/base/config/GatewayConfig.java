package org.base.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.dto.ApiDTO;
import org.base.exception.SystemException;
import org.base.ultilities.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class GatewayConfig {

    public String CONFIG_DIR = System.getProperty("user.dir") + File.separator  +
                                    "gateway/config" + File.separator;
    public static final String API_ROOT_PATH = "/api/";

    public static List<String> SERVICE_LIST = new ArrayList<>();


    public static final Map<String, String> SERVICE_MAP = new HashMap<>();

    private void loadConfig() {
        try {
            log.info("======= Loading GatewayConfig config... =======");
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream(CONFIG_DIR + "gateway.properties"), "UTF-8"));
            Enumeration<?> e = properties.propertyNames();

            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);

                System.out.println("+ key: " + key + " => value: " + value);
                if (key.equalsIgnoreCase("service.list")) {
                    String[] services = value.split(",");
                    SERVICE_LIST = Arrays.asList(services);

                    if(services.length != 0) {
                        for (String service:services) {
                            loadAPIJson(service);
                        }
                    }
                }
            }
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

                });
            }
        } catch (FileNotFoundException ex) {
            throw new SystemException("File gateway.properties bị lỗi!!");
        } catch (IOException ex) {
            throw new SystemException("Lấy data từ file gateway.properties bị lỗi!!");
        }
    }

    public static void main(String[] args) {
        new GatewayConfig().loadConfig();
    }
    /*public String validate(String requestPath, String service, String method) {
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
        RabbitMapper rabbitMapper = GatewayConfig.SERVICE_PATH_MAP.getOrDefault(requestPath + "  " + method, null);
        if(rabbitMapper == null){
            return "Request path không tồn tại";
        }
        if(GatewayConfig.SERVICE_PATH_SET_PRIVATE.contains(requestPath)){
            return "Request path không mở public";
        }

        return null;
    }*/
}
