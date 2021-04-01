package com.zcw.cblog.file.service.impl;

import com.zcw.cblog.common.utils.RegExUtils;
import com.zcw.cblog.common.vo.FileVo;
import com.zcw.cblog.file.dao.FileDao;
import com.zcw.cblog.file.entity.FileEntity;
import com.zcw.cblog.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Chrisz
 * @date 2021/3/16 - 19:09
 */
@Service
public class FileServiceImpl implements FileService {


    @Autowired
    FileDao fileDao;
    @Override
    public Integer uploadFiles(String newFileName,  String aid, String uid,String path, long size) {
        FileEntity fileEntity = new FileEntity();

        Long ad = Long.parseLong(aid);
        Long ud = Long.parseLong(uid);
        String s = size+"kb";

        fileEntity.setFileName(newFileName);
        fileEntity.setPath(path);
        fileEntity.setAid(ad);
        fileEntity.setUid(ud);
        fileEntity.setFileSize(s);
        fileEntity.setUploadTime(new Date());
        return fileDao.uploadFiles(fileEntity);
    }

    @Override
    public List<FileVo> getFilesByAid(Integer aid) {

        return fileDao.getFilesByAid(aid);
    }
}
