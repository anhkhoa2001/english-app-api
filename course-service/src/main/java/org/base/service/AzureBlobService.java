package org.base.service;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.base.dto.common.EFile;

import java.util.List;
import java.util.Map;

public interface AzureBlobService {

    List<String> listBlobs();
    Map<String, Object> listFileShares();
    String upload(EFile file);

    ByteArrayOutputStream getDataFile(String filename);
}
