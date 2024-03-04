package org.base.dao.impl;

import org.base.dao.FileDao;
import org.base.dto.FileDTO;
import org.base.exception.ValidationException;
import org.base.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileDaoImpl implements FileDao {

    @Override
    public String getUrlInFile(List<FileDTO> files) {
        if(!StringUtil.isListEmpty(files)) {
            return (String) files.get(0).getResponse()
                    .getOrDefault("default", null);
        }

        throw new ValidationException("Type image invalid!!");
    }
}
