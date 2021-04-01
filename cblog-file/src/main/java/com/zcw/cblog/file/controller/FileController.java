package com.zcw.cblog.file.controller;

import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.common.utils.DateUtils;
import com.zcw.cblog.common.vo.FileVo;
import com.zcw.cblog.file.service.FileService;
import com.zcw.cblog.file.service.NginxService;
import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chrisz
 * @date 2021/3/16 - 19:08
 */

@Controller
public class FileController {

    @Autowired
    NginxService  nginxService;
    @Autowired
    FileService fileService;
    /**
     * 上传文件方法
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/upload/file/{uid}/{aid}", "/upload/avatar/{uid}"})
    @ResponseBody
    public Map<String, Object> uploadFile(HttpServletRequest request,
                                          @PathVariable(value = "aid",required = false) String aid,
                                          @PathVariable(value = "uid",required = false) String uid) {


        String res = DateUtils.date2String(new Date(), "yyyyMMddHHmmssSS");
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");//file是form-data中二进制字段对应的name
        assert multipartFile != null;
        long size = multipartFile.getSize() / 1024;


        Map<String, Object> map = new HashMap<String, Object>(16);
        Map<String, Object> data = new HashMap<String, Object>(16);
        if (multipartFile.getSize() == 0){
            //上传失败
            map.put("code", 1);
            //提示消息
            map.put("msg", "选择要上传的文件");
            return map;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        //新的文件名称用时间代替。
        String newFileName = res + originalFilename.substring(originalFilename.lastIndexOf("."));
        //nginx
        String p = "/static/cblog/file/";
        Integer result = nginxService.uploadPicture(multipartFile,newFileName,p);

        if (result == 1) {

            String path = p+newFileName;
            Integer integer = fileService.uploadFiles(newFileName,aid,uid,path,size);

            if (integer ==1){
                //上传成功
                map.put("code", 0);
                //提示消息
                map.put("msg", "上传成功");
            }
            else {
                //上传失败
                map.put("code", 1);
                //提示消息
                map.put("msg", "上传失败");
            }

        } else {
            //上传失败
            map.put("code", 1);
            //提示消息
            map.put("msg", "上传失败");
        }
        map.put("data", data);
        //图片url
        //data.put("src", fileUrl);
        //图片名称，这个会显示在输入框里   可不写
        //data.put("title", newFileName);
        return map;
    }

    /**
     * 返回文件展示
     * @param aid
     * @return
     */
    @RequestMapping(value = {"/getFilesByAid"})
    @ResponseBody
    public List<FileVo> getFilesByAid(@RequestParam("aid") Integer aid){

        List<FileVo> fileVo = fileService.getFilesByAid(aid);
        return fileVo;
    }
}
