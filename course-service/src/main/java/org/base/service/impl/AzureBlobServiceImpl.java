
package org.base.service.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.common.ParallelTransferOptions;
import com.azure.storage.common.ProgressReceiver;
import com.azure.storage.file.share.ShareClient;
import com.azure.storage.file.share.ShareDirectoryClient;
import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.models.ShareFileItem;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.base.dto.common.EFile;
import org.base.exception.SystemException;
import org.base.service.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class AzureBlobServiceImpl implements AzureBlobService {

    @Autowired
    BlobContainerClient blobContainerClient;

    @Autowired
    ShareClient shareClient;

    @Override
    public List<String> listBlobs() {
        PagedIterable<BlobItem> items = blobContainerClient.listBlobs();
        List<String> names = new ArrayList<String>();
        for (BlobItem item : items) {
            names.add(item.getName());
        }
        return names;
    }

    @Override
    public Map<String, Object> listFileShares() {
        ShareDirectoryClient directory = shareClient.getRootDirectoryClient();
        PagedIterable<ShareFileItem> items = directory.listFilesAndDirectories();
        Map<String, Object> map = new HashMap<>();
        for (ShareFileItem item : items) {

        }
        return map;
    }


    @Override
    public ByteArrayOutputStream getDataFile(String filename) {
        ShareFileClient share = shareClient.getDirectoryClient("js").getFileClient(filename);
        ShareDirectoryClient shareRoot = shareClient.getRootDirectoryClient();
        /*shareRoot.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        share.download(byteArrayOutputStream);*/
        return null;
    }

    public String upload(EFile file){
        try {
            byte[] dataFile = Base64.getDecoder().decode(file.getData());
            ShareFileClient share = shareClient.createFile(file.getFileName(), dataFile.length);
            // Verify that the ShareFileClient exists
            if (!share.exists()) {
                throw new SystemException("ShareFileClient does not exist: " + file.getFileName());
            }
            InputStream input = new ByteArrayInputStream(dataFile);
            share.upload(input, dataFile.length,
                    new ParallelTransferOptions(512, 5,
                            new SimpleProgressReceiver(), 1000));

            return file.getFileName();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SystemException("Upload fiile to could failed!!");
        }
    }


    class SimpleProgressReceiver implements ProgressReceiver {
        @Override
        public void handleProgress(long progress) {}

        @Override
        public void reportProgress(long l) {}
    }
}