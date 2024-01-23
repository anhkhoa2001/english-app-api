package org.base.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.base.exception.SystemException;
import org.base.model.CourseModel;
import org.base.repositories.CourseRepository;
import org.base.service.CourseService;
import org.base.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AmazonS3 getAmazonS3Client;

    @Value("${amazon.s3.bucket}")
    private String s3BucketName;

    @Async
    public S3ObjectInputStream findByName(String fileName) {
        log.info("Downloading file with name {}", fileName);
        S3Object object = getAmazonS3Client.getObject(s3BucketName, fileName);

        return object.getObjectContent();
    }
    @Async
    public URL save(final MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();

            final String fileName = multipartFile.getOriginalFilename();
            log.info("Uploading file with name {}", fileName);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            final PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, inputStream, metadata);
            PutObjectResult result = getAmazonS3Client.putObject(putObjectRequest);

            URL url = getAmazonS3Client.getUrl(s3BucketName, fileName);
            log.info("{}", result);
            log.info("URL {}", url);
            log.info("public url {}", url.getPath());

            return url;
        } catch (AmazonServiceException e) {
            log.error("Error {} occurred while uploading file", e.getLocalizedMessage());
        } catch (IOException ex) {
            log.error("Error {} occurred while deleting temporary file", ex.getLocalizedMessage());
        }

        return null;
    }

    @Override
    public List<CourseModel> getAll() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<CourseModel> save(CourseModel courseModel) {

        try {
            List<CourseModel> course = courseRepository.getByCourseName(courseModel.getCourseName());
            if(course.isEmpty()) {
                courseRepository.save(courseModel);
            } else {
                log.error("Trung ban ghi!!");
            }
            return courseRepository.findAll();
        } catch (Exception e) {
            ExceptionUtils.printException(e, "", "");
            throw new SystemException("trung ban ghi");
        }
    }
}
