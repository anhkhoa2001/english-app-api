package org.base.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.base.dao.FileDao;
import org.base.exception.AppException;
import org.base.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3_bucket_name}")
    private String s3BucketName;

    @Value("${spring.amazon.s3.url}")
    private String url;

    @Autowired
    private FileDao fileDao;

    @Override
    @Transactional
    public Map<String, URL> saveFileToCloud(String filename, InputStream content, long size, String username) {
        if(username == null) {
            username = "admin";
        }
        Map<String, URL> map = new HashMap<>();
        String fileName = fileDao.createPath(username, filename);
        try {
            S3Object s3Object = s3Client.getObject(s3BucketName, fileName);
            if(s3Object != null) {
                filename = "duplicate-" + UUID.randomUUID() + "-" + filename;
                fileName = fileDao.createPath(username, filename);
            }
        } catch (AmazonServiceException e) {
            log.error("Get file on s3 {}", e.getErrorMessage());
        }

        try {
            Bucket bucket = fileDao.createBucket(s3BucketName);
            InputStream inputStream = content;

            log.info("Uploading file with name {}", fileName);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(size);
            final PutObjectRequest putObjectRequest = new PutObjectRequest(bucket.getName(), fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicReadWrite);
            s3Client.putObject(putObjectRequest);

            URL url = s3Client.getUrl(bucket.getName(), fileName);
            log.info("Upload to bucket {} success.......", bucket.getName());
            log.info("URL {}", url);
            map.put("default", url);

            return map;
        } catch (AmazonServiceException e) {
            log.error("Error {} occurred while uploading file", e.getLocalizedMessage());
        }

        return new HashMap<>();
    }

    /*@Override
    public Object getFileByPagination(ImageS3Request request, String username) {
        return secFileRepository.getFilesByCondition(request, username, s3BucketName);
        *//*if(!StringUtil.stringIsNullOrEmty(request.getUploadDate())) {
            String[] parts = request.getUploadDate().split("/");

            prefix += parts[2] + "/" + Integer.parseInt(parts[1]) + "/" + Integer.parseInt(parts[0]);
        }

        try {
            ListObjectsRequest objectsRequest = null;
            if(request.getPage() == 1) {
                objectsRequest = new ListObjectsRequest().withBucketName(s3BucketName)
                        .withPrefix(prefix)
                        .withMaxKeys(request.getPageSize());
            } else {
                objectsRequest = new ListObjectsRequest().withBucketName(s3BucketName)
                        .withPrefix(prefix)
                        .withMarker(request.getMarker())
                        .withMaxKeys(request.getPageSize());
            }
            ObjectListing objectListing = s3Client.listObjects(objectsRequest);
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

            objectSummaries.forEach(e -> {
                e.setStorageClass(url + e.getKey());
            });
            result.put("data", objectSummaries);
            result.put("totalRecord", getTotalFiles(username));

            return result;
        } catch (AmazonServiceException e) {
            log.error("Error {} occurred while uploading file", e.getLocalizedMessage());
        }*//*
    }*/

    @Override
    public void deleteObjectS3(String filename, String username) {
        try {
            s3Client.deleteObject(s3BucketName, filename);
        } catch (AmazonServiceException e) {
            log.error("Occur error when delete object on s3 {}", e.getErrorMessage());
        }
    }

    @Override
    public Object getAll() {
        try {
            ListObjectsRequest objectsRequest = new ListObjectsRequest().withBucketName(s3BucketName);

            ObjectListing objectListing = s3Client.listObjects(objectsRequest);
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

            objectSummaries.forEach(e -> {
                e.setStorageClass(url + e.getKey());
            });

            return objectSummaries;
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }
}
