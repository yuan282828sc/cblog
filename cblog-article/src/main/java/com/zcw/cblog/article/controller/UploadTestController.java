//package com.zcw.cblog.article.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zcw.cblog.article.service.NginxService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author Chrisz
// * @date 2021/2/26 - 22:30
// */
//@RestController
//@Slf4j
//public class UploadTestController {
//    @Autowired
//    private NginxService nginxService;
//    /**
//     * 可上传图片、视频，只需在nginx配置中配置可识别的后缀
//     */
//    @PostMapping("/upload")
//    public String pictureUpload(@RequestParam(value = "file") MultipartFile uploadFile) {
//        long begin = System.currentTimeMillis();
//        String json = "";
//        try {
//            Object result = nginxService.uploadPicture(uploadFile);
//            json = new ObjectMapper().writeValueAsString(result);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        long end = System.currentTimeMillis();
//        log.info("任务结束，共耗时：[" + (end-begin) + "]毫秒");
//        return json;
//    }
//    @PostMapping("/uploads")
//    public Object picturesUpload(@RequestParam(value = "file") MultipartFile[] uploadFile) {
//        long begin = System.currentTimeMillis();
//        Map<Object, Object> map = new HashMap<>(10);
//        int count = 0;
//        for (MultipartFile file : uploadFile) {
//            Object result = nginxService.uploadPicture(file);
//            map.put(count, result);
//            count++;
//        }
//        long end = System.currentTimeMillis();
//        log.info("任务结束，共耗时：[" + (end-begin) + "]毫秒");
//        return map;
//    }
//
//}
