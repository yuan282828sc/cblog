package com.zcw.cblog.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * @Description TODO:异常页面
 */
@SuppressWarnings("ALL")
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        int status404 = 404;
        int status403 = 403;
        //获取statusCode:404
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        //找不到页面
        if(statusCode == status404){
            return "other/404";
        }else if (statusCode == status403){
            return "other/403";
        }else{
            //其他错误就当服务器维护处理
            return "other/notice";
        }
    }
    @Override
    public String getErrorPath() {
        return "/error";
    }
}

