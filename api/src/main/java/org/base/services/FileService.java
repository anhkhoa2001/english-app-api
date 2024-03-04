package org.base.services;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public interface FileService {

    Map<String, URL> saveFileToCloud(String filename, InputStream content, long size, String username);

    //Object getFileByPagination(ImageS3Request request, String username);
    URL getPresignURL(String filename);

    void deleteObjectS3(String filename, String username);
}
