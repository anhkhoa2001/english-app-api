package org.base.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary configCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dwqrocbjv",
                "api_key", "855845865455944",
                "api_secret", "J02OiKFOVCFtD8OAaAkMsIhn3Uc"));
    }
}
