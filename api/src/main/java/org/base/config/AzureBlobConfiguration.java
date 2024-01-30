package org.base.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.file.share.ShareClient;
import com.azure.storage.file.share.ShareServiceClient;
import com.azure.storage.file.share.ShareServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobConfiguration {

    @Value("${azure.storage.connection.string}")
    private String connectionString;

    @Value("${azure.storage.container.name}")
    private String containerName;

    @Value("${azure.storage.file-share.name}")
    private String shareName;

    @Bean
    public BlobServiceClient clobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }


    @Bean
    public ShareServiceClient shareServiceClient() {
        return new ShareServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }


    @Bean
    public ShareClient shareClient() {
        return shareServiceClient().getShareClient(shareName);
    }

    @Bean
    public BlobContainerClient blobContainerClient() {
        return clobServiceClient()
                .getBlobContainerClient(containerName);
    }
}
