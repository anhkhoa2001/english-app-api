package org.base.service;

import org.base.dto.common.EFile;

import java.io.IOException;
import java.util.List;

public interface AzureBlobService {

    List<String> listBlobs();
    List<String> listFileShares();
    String upload(EFile file);
}
