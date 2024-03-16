package org.base.dao.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.base.dao.FileDao;
import org.base.dto.FileDTO;
import org.base.exception.AppException;
import org.base.exception.ValidationException;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FileDaoImpl implements FileDao {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3_bucket_name}")
    private String s3BucketName;

    @Value("${spring.amazon.s3.url}")
    private String url;

    @Override
    public String getUrlInFile(List<FileDTO> files) {
        if(!StringUtil.isListEmpty(files)) {
            FileDTO file = files.get(0);
            return file.getResponse().getOrDefault("default", "").toString();
        }

        throw new ValidationException("Type image invalid!!");
    }

    @Override
    public S3Object getFileByFilename(String filename) {
        try {
            return s3Client.getObject(s3BucketName, filename);
        } catch (AmazonServiceException e) {
            log.error("Get file on s3 {}", e.getErrorMessage());
        }
        return null;
    }


    @Override
    public String createPath(String username, String filename) {
        if(username == null) {
            username = "admin";
        }
        return username + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() +
                "/" + LocalDate.now().getDayOfMonth() + "/" + filename;
    }

    @Override
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

    @Override
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

    @Override
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
    /*public void checkTypeFile(S3Object s3Object) {
        List<String> imageFiles = new ArrayList<>();
        List<String> videoFiles = new ArrayList<>();
        List<String> audioFiles = new ArrayList<>();
        String fileName = s3Object.getKey();
        String fileExtension = getFileExtension(fileName);
        if (fileExtension != null) {
            switch (fileExtension.toLowerCase()) {
                case "png":
                case "jpg":
                case "jpeg":
                case "gif":
                case "bmp":
                case "tif":
                case "tiff":
                    imageFiles.add(fileName);
                    break;
                case "mp4":
                case "avi":
                case "mkv":
                case "mov":
                case "wmv":
                case "flv":
                case "webm":
                    main(fileName);
                    videoFiles.add(fileName);
                    break;
                case "mp3":
                case "wav":
                case "ogg":
                case "flac":
                case "aac":
                case "wma":
                    audioFiles.add(fileName);
                    break;
                default:
                    // Unknown file type
                    break;
            }
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return null;
    }

    public void main(String fileName) {
        long objectSize = 1000; // Replace with the actual size of your S3 object in bytes

        // Create a temporary file to download the S3 object
        try {
            // Download a part of the S3 object to the temporary file
            GetObjectRequest getObjectRequest = new GetObjectRequest(s3BucketName, fileName)
                    .withRange(0, Math.min(10 * 1024 * 1024, objectSize - 1)); // Download the first 10 MB or less

            ObjectMetadata objectMetadata = s3Client.getObjectMetadata(new GetObjectMetadataRequest(s3BucketName, fileName));


            long contentLength = objectMetadata.getContentLength();

            // Get the video bitrate from metadata if available
            String bitrateString = objectMetadata.getUserMetaDataOf("bitrate");
            int bitrateKbps = 0; // Default bitrate if not available
            if (bitrateString != null) {
                bitrateKbps = Integer.parseInt(bitrateString); // Assuming bitrate is in kilobits per second
            }

            // Convert bitrate to bits per second
            long bitrateBps = bitrateKbps * 1000; // Convert kilobits to bits

            // Calculate duration
            double durationSeconds = (double) contentLength / (double) bitrateBps;

            System.out.println("Video duration: " + durationSeconds + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
