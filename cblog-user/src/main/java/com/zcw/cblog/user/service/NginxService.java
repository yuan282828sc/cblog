package com.zcw.cblog.user.service;

import com.zcw.cblog.common.utils.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Chrisz
 * @date 2021/2/26 - 22:31
 */
@Service
@Slf4j
public class NginxService {
    public Integer uploadPicture(MultipartFile uploadFile,String newFileName,String p) {
//        //1、给上传的图片生成新的文件名
//        //1.1获取原始文件名
//        String oldName = uploadFile.getOriginalFilename();
//        //1.2使用IDUtils工具类生成新的文件名，新文件名 = newName + 文件后缀
//        String newName = IDUtils.genImageName();
//        assert oldName != null;
//        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        //1.3生成文件在服务器端存储的子目录
        //String filePath = new DateTime().toString("/yyyyMMdd/");
        //2、把图片上传到图片服务器
        //2.1获取上传的io流
        InputStream input = null;
        try {
            input = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2.2调用FtpUtil工具类进行上传
        return FtpUtil.putImages(input, newFileName,p);
    }
}
