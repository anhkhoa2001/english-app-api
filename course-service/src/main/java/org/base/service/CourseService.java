package org.base.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.base.model.CourseModel;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

public interface CourseService {

    List<CourseModel> getAll();

    List<CourseModel> save(CourseModel courseModel);

    S3ObjectInputStream findByName(String fileName);

    URL save(final MultipartFile multipartFile);
}
