package org.base.service.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.management.Resource;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.common.ParallelTransferOptions;
import com.azure.storage.common.ProgressReceiver;
import com.azure.storage.file.share.ShareClient;
import com.azure.storage.file.share.ShareDirectoryClient;
import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.models.ShareFileItem;
import org.base.dto.common.EFile;
import org.base.exception.SystemException;
import org.base.service.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
    public List<String> listFileShares() {
        ShareDirectoryClient directory = shareClient.getRootDirectoryClient();
        PagedIterable<ShareFileItem> items = directory.listFilesAndDirectories();
        List<String> names = new ArrayList<String>();
        for (ShareFileItem item : items) {
            if (item.isDirectory()) {
                names.add("Directory: " + item.getName());
            } else {
                names.add("File: " + item.getName());
            }
        }
        return names;
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
