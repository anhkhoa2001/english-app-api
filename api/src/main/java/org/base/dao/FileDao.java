package org.base.dao;

import org.base.dto.FileDTO;

import java.util.List;

public interface FileDao {

    String getUrlInFile(List<FileDTO> files);
}
