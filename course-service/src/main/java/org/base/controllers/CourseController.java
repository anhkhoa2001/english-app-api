package org.base.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.base.dto.common.EFile;
import org.base.exception.SystemException;
import org.base.model.CourseModel;
import org.base.service.AzureBlobService;
import org.base.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private AzureBlobService azureBlobService;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity getAllCourse() {
        return ResponseEntity.ok(azureBlobService.listBlobs());
    }

    @GetMapping("/get-in-file-share")
    public ResponseEntity getInFileShare() {
        return ResponseEntity.ok(azureBlobService.listFileShares());
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestBody EFile file) {
        return ResponseEntity.ok(azureBlobService.upload(file));
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam String filename, HttpServletResponse response) {
        try {
            ByteArrayOutputStream output = azureBlobService.getDataFile(filename);
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());

            BufferedInputStream input = new BufferedInputStream(new ByteArrayInputStream(output.toByteArray()));
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = input.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("read data file failed!!");
        }
    }

    @Autowired
    private Cloudinary configCloudinary;

    @GetMapping("/test-cloudinary")
    public ResponseEntity testCloud(@RequestParam String id) throws Exception {
        ApiResponse apiResponse = configCloudinary.api().resourceByAssetID(id, ObjectUtils.emptyMap());
        System.out.println(apiResponse);

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/get-all-course-db")
    public ResponseEntity getAllCourse(@RequestParam String id) throws Exception {
        return ResponseEntity.ok(courseService.getAll());
    }

    @GetMapping("/save-course-db")
    public ResponseEntity saveCourse(@RequestParam String id) throws Exception {
        CourseModel course = CourseModel.builder().code(UUID.randomUUID().toString())
                .courseName(id)
                .createAt(new Date()).build();

        return ResponseEntity.ok(courseService.save(course));
    }

}
