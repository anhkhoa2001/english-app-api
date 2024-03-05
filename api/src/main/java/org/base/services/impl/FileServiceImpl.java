package org.base.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    @Transactional
    public Map<String, URL> saveFileToCloud(String filename, InputStream content, long size, String username) {
        if(username == null) {
            username = "admin";
        }
        Map<String, URL> map = new HashMap<>();
        String fileName = createPath(username, filename);
        try {
            S3Object s3Object = s3Client.getObject(s3BucketName, fileName);
            if(s3Object != null) {
                filename = "duplicate-" + UUID.randomUUID() + "-" + filename;
                fileName = createPath(username, filename);
            }
        } catch (AmazonServiceException e) {
            log.error("Get file on s3 {}", e.getErrorMessage());
        }

        try {
            Bucket bucket = createBucket(s3BucketName);
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

    public S3Object getFileByFilename(String filename) {
        try {
            return s3Client.getObject(s3BucketName, filename);
        } catch (AmazonServiceException e) {
            log.error("Get file on s3 {}", e.getErrorMessage());
        }
        return null;
    }

    public URL getPresignURL(String filename) {
        try {
            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = Instant.now().toEpochMilli();
            expTimeMillis += 1000;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3BucketName,
                    filename)
                    .withMethod(HttpMethod.GET);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            System.out.println("Pre-Signed URL: " + url.toString());
            return url;
        } catch (AmazonServiceException e) {
            log.error("error {}", e.getErrorMessage());
        } catch (SdkClientException e) {
            log.error("error {}", e.getMessage());
        }


        return null;
    }

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

    private String createPath(String username, String filename) {
        return username + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() +
                "/" + LocalDate.now().getDayOfMonth() + "/" + filename;
    }


    public Bucket createBucket(String bucketName) {
        Bucket b = null;
        if (s3Client.doesBucketExistV2(bucketName)) {
            log.warn("Bucket {} already exists......", bucketName);
            b = getBucket(bucketName);
        } else {
            try {
                b = s3Client.createBucket(bucketName);
                s3Client.setBucketAcl(bucketName, CannedAccessControlList.PublicReadWrite);
            } catch (AmazonS3Exception e) {
                log.error("Error {} occurred while create bucket", e.getErrorMessage());
                throw new AppException(e.getErrorMessage());
            }
        }

        return b;
    }

    public Bucket getBucket(String bucketName) {
        Bucket bucket = null;
        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucketName)) {
                bucket = b;
                break;
            }
        }
        return bucket;
    }
}
