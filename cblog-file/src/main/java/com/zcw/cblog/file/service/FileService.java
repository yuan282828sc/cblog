package com.zcw.cblog.file.service;

import com.zcw.cblog.common.vo.FileVo;

import java.util.List;

/**
 * @author Chrisz
 * @date 2021/3/16 - 19:09
 */
public interface FileService {


    Integer uploadFiles(String newFileName,  String aid, String uid,String path, long size);


    List<FileVo> getFilesByAid(Integer aid);
}

