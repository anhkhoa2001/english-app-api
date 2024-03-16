package org.base.dao;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import org.base.dto.FileDTO;

import java.net.URL;
import java.util.List;

public interface FileDao {

   String getUrlInFile(List<FileDTO> files);

    S3Object getFileByFilename(String filename);

    String createPath(String username, String filename);

    Bucket createBucket(String bucketName);

    Bucket getBucket(String bucketName);

    URL getPresignURL(String filename);
}
