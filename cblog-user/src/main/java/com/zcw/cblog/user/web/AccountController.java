package com.zcw.cblog.user.web;


import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import com.zcw.cblog.common.constant.AuthServerConstant;
import com.zcw.cblog.common.statusenum.AccountStatusEnum;
import com.zcw.cblog.common.statusenum.VisitorStatusEnum;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.common.utils.DateUtils;
import com.zcw.cblog.common.utils.VerCodeGenerateUtil;
import com.zcw.cblog.common.vo.ReslutTypeVo;
import com.zcw.cblog.user.entity.UserEntity;
import com.zcw.cblog.user.feign.MessageFeignService;
import com.zcw.cblog.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO:所有人 可进行账号相关操作
 */
@Controller
//@RequestMapping("/user")
public class AccountController {

    /**
        创建线程池对象
     */
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private MessageFeignService messageFeignService;

    /**
     * 注册页
     * @return
     */
    @GetMapping("/reg")
    public String reg() {
        return "user/reg";
    }


    /**
     * 登陆过再去登录页自动跳转到首页
     * @param session
     * @return
     */
    @GetMapping("/reg.html")
    public String regPage(HttpSession session){

        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute==null){
            return "user/reg";
        }else{
            return "redirect:http://cblog.com";
        }

    }
    /**
     * 注册账号
     *
     * @param username
     * @param password
     * @param verCode
     * @param request
     * @return
     */
    @PostMapping("/reg")
    @ResponseBody
    public ReslutTypeVo reg(String username,
                            String password,
                            String verCode,
                            HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, String> codeMap = (Map<String, String>) session.getAttribute("verCode");
        String code = null;
        String email = null;
        try {
            code = codeMap.get("code");
            email = codeMap.get("email");
        } catch (Exception e) {
            //验证码过期，或未找到  ---验证码无效
            return new ReslutTypeVo(VisitorStatusEnum.Email_Captcha_Invalid);
        }
        //验证码判断
        if (!verCode.toUpperCase().equals(code.toUpperCase()) || !username.equals(email)) {
            return new ReslutTypeVo(VisitorStatusEnum.Email_Captcha_Fail);
        }
        //验证码使用完后session删除
        session.removeAttribute("verCode");
        UserEntity user = userService.findUserByName(username);
        //用户名是否可用
        if (user != null) {
            return new ReslutTypeVo(VisitorStatusEnum.Account_Registered);
        }
        Integer integer = userService.addUser(username, password, request);
        //是否插入数据成功
        if (integer == null || !integer.equals(1)) {
            return new ReslutTypeVo(VisitorStatusEnum.Register_Fail);
        }
        return new ReslutTypeVo(VisitorStatusEnum.Register_OK);
    }

    /**
     * 登录页
     * @return
     */
    @GetMapping("/login")
    public String toLogin() {
        return "user/login";
    }


    /**
     * 登陆过再去登录页自动跳转到首页
     * @param session
     * @return
     */
    @GetMapping("/login.html")
    public String loginPage(HttpSession session){

        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute==null){
            return "user/login";
        }else{
            return "redirect:http://cblog.com";
        }

    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param verCode
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ReslutTypeVo login(String username,
                              String password,
                              String verCode,
                              Boolean rememberMe,
                              HttpServletRequest request,
                              HttpSession sessionRedis) {
        // 获取session中的验证码
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        // 判断验证码                   .trim().toLowerCase() 删除空格  变成小写
        if (verCode == null || !sessionCode.equals(verCode.trim().toLowerCase())) {
            //"验证码不正确"
            return new ReslutTypeVo(VisitorStatusEnum.Captcha_Fail);
        }
        //获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        UserEntity user = userService.findUserByName(username);
        //没有这个人
        if (user == null) {
            //抛出异常
            return new ReslutTypeVo(VisitorStatusEnum.Login_Fail);
        }
        //账号状态 1正常
        Integer status = user.getStatus();
        if (!status.equals(1)) {
            return new ReslutTypeVo(AccountStatusEnum.ABNORMAL);
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            //设置记住我
            token.setRememberMe(rememberMe);
            //之后去realm登录验证
            subject.login(token);
            //更新登录数据。。
            //为了安全起见，自己封装一个vo类，防止重要信息泄露
            UserTo userVo = userService.findUserById(user.getUid());
            //记录新消息数
            Long rid = userVo.getUid();
            Integer newMessageTotal = messageFeignService.findNewMessageTotal(rid);
            //放回前端页面
            //Session session = subject.getSession();
            if (newMessageTotal > 0) {
                sessionRedis.setAttribute("newMessageTotal", newMessageTotal);
            }
            sessionRedis.setAttribute("loginUser", userVo);
            //单位ms
            //但是Shiro会把这个时间转成:s，而且是会舍掉小数部分，这样我设置的是-1ms，转成s后就是0s，马上就过期了。所有要是除以1000以后还是负数，必须设置小于-1000
            //正负都可以，为负数时表示永不超时
//            session.setTimeout(1800000);


//            HttpSession session1 = request.getSession();
//            session1.setAttribute("loginUser", userVo);
//            session1.setMaxInactiveInterval(3600);


            //更新登录ip 和登录时间一起。
            String remoteAddr = request.getRemoteAddr();
            user.setIp(remoteAddr);
            //更新最后登录时间
            userService.updateLastTime(user);
            //登录成功直接转跳
            //回到login.js 跳转到首页
            return new ReslutTypeVo(VisitorStatusEnum.Login_OK);
        } catch (UnknownAccountException e) {
            //"账号错误"
            return new ReslutTypeVo(VisitorStatusEnum.Login_Fail);
        } catch (IncorrectCredentialsException e) {
            //"密码错误"
            return new ReslutTypeVo(VisitorStatusEnum.Login_Fail);
        } catch (Exception e) {
            //登录失败
            return new ReslutTypeVo(VisitorStatusEnum.Login_Fail);
        }
    }

    //用户退出
    @GetMapping("/logout")
    public String logout(HttpSession session){

        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        //session失效
        session.removeAttribute("loginUser");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //重定向到首页
        return "redirect:http://cblog.com";

    }

    /**
     * 忘记密码页
     * @return
     */
    @GetMapping("/forget")
    public String forget() {
        return "user/forget";
    }

    /**
     * 找回密码
     *
     * @param username
     * @param password
     * @param verCode
     * @param request
     * @return
     */
    @PostMapping(value = {"/forget", "/change"})
    @ResponseBody
    public ReslutTypeVo forget(String username,
                                String password,
                                String verCode,
                                HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, String> codeMap = (Map<String, String>) session.getAttribute("verCode");
        String code = null;
        String email = null;
        String path = "change";
        try {
            code = codeMap.get("code");
            email = codeMap.get("email");
        } catch (Exception e) {
            //验证码过期，或未找到  ---验证码无效
            return new ReslutTypeVo(VisitorStatusEnum.Email_Captcha_Invalid);
        }
        //验证码判断
        if (!verCode.toUpperCase().equals(code.toUpperCase()) || !username.equals(email)) {
            return new ReslutTypeVo(VisitorStatusEnum.Email_Captcha_Fail);
        }
        //验证码使用完后session删除
        session.removeAttribute("verCode");
        Integer integer = userService.modifyUserPassword(username, password);
        //是否插入数据成功
        if (integer == null || !integer.equals(1)) {
            return new ReslutTypeVo(VisitorStatusEnum.Password_Modify_Fail);
        }
        //判断uri是那边串过来的--如果是用户修改密码，则转跳需要登录页面，退出账户。
        String requestUri = request.getRequestURI();
        String uri = requestUri.substring(requestUri.lastIndexOf("/") + 1);
        if (path.equals(uri)) {
            session.removeAttribute("loginUser");
        }
        return new ReslutTypeVo(VisitorStatusEnum.Password_Modify_OK);
    }



    /**
     * 验证码引入
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 三个参数分别为宽、高、位数
        GifCaptcha gifCaptcha = new GifCaptcha(130, 48, 4);
        // 设置字体
        // 有默认字体，可以不用设置
        gifCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));
        // 设置类型，纯数字、纯字母、字母数字混合
        gifCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        // 验证码存入session
        request.getSession().setAttribute("captcha", gifCaptcha.text().toLowerCase());
        // 输出fig图片流
        gifCaptcha.out(response.getOutputStream());
    }

    /**
     * 获取邮箱验证码 以邮箱作为username
     * 开启异步发送
     *
     * @param email /reg/sendEmail
     * @throws MessagingException
     */
    @GetMapping("/reg/sendEmail")
    @ResponseBody
    public ReslutTypeVo sendEmail(String email, String emailStatus, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String status1 = "1000";
        String status2 = "1001";
        String  code ="email";
        //验证码
        String verCode = VerCodeGenerateUtil.getVerCode();
        //发送时间
        String time = DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new HashMap<>(16);
        map.put("code", verCode);
        map.put("email", email);
        session.setAttribute("verCode", map);
        Map<String, String> codeMap = (Map<String, String>) session.getAttribute("verCode");
        //设置验证码有效时间，并放入session
        try {
            scheduledExecutorService.schedule(new Thread(() -> {
                if (email.equals(codeMap.get(code))) {
                    session.removeAttribute("verCode");
                }
            }), 5 * 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String title = "";
        if (emailStatus != null && status1.equals(emailStatus)) {
            title = "【Cblog】 注册账号验证码";
        } else if (emailStatus != null && status2.equals(emailStatus)) {
            //找回密码前先查询是否有这个账户
            UserEntity user = userService.findUserByName(email);
            if (user == null) {
                return new ReslutTypeVo(VisitorStatusEnum.Account_Invalid);
            }
            title = "【Cblog】 账号找回密码验证码";
        } else {
            //发送失败--服务器繁忙
            return new ReslutTypeVo(VisitorStatusEnum.Server_Busy);
        }
        MimeMessage mimeMessage = null;
        MimeMessageHelper helper = null;
        try {
            //发送复杂的邮件
            mimeMessage = mailSender.createMimeMessage();
            //组装
            helper = new MimeMessageHelper(mimeMessage, true);
            //正文
            helper.setSubject(title);

            helper.setText("<h3>\n" +
                    "\t<span style=\"font-size:16px;\">亲爱的用户：</span> \n" +
                    "</h3>\n" +
                    "<p>\n" +
                    "\t<span style=\"font-size:14px;\">&nbsp;&nbsp;&nbsp;&nbsp;</span><span style=\"font-size:14px;\">&nbsp; <span style=\"font-size:16px;\">&nbsp;&nbsp;您好！您正在进行邮箱验证，本次请求的验证码为：<span style=\"font-size:24px;color:#FFE500;\"> " + verCode + "</span>,本验证码5分钟内有效，请在5分钟内完成验证。（请勿泄露此验证码）如非本人操作，请忽略该邮件。(这是一封自动发送的邮件，请不要直接回复）</span></span>\n" +
                    "</p>\n" +
                    "<p style=\"text-align:right;\">\n" +
                    "\t<span style=\"background-color:#FFFFFF;font-size:16px;color:#000000;\"><span style=\"color:#000000;font-size:16px;background-color:#FFFFFF;\"><span class=\"token string\" style=\"font-family:&quot;font-size:16px;color:#000000;line-height:normal !important;background-color:#FFFFFF;\">Share社区</span></span></span> \n" +
                    "</p>\n" +
                    "<p style=\"text-align:right;\">\n" +
                    "\t<span style=\"background-color:#FFFFFF;font-size:14px;\"><span style=\"color:#FF9900;font-size:18px;\"><span class=\"token string\" style=\"font-family:&quot;font-size:16px;color:#000000;line-height:normal !important;\"><span style=\"font-size:16px;color:#000000;background-color:#FFFFFF;\">" + time + "</span><span style=\"font-size:18px;color:#000000;background-color:#FFFFFF;\"></span></span></span></span> \n" +
                    "</p>", true);
            //收件人
            helper.setTo(email);
            //发送方
            helper.setFrom("1582691494@qq.com");
            try {
                mailSender.send(mimeMessage);
            } catch (MailException e) {
                return new ReslutTypeVo(VisitorStatusEnum.Email_Invalid);
            }
        } catch (MessagingException e) {
            //发送失败--服务器繁忙
            return new ReslutTypeVo(VisitorStatusEnum.Server_Busy);
        }
        return new ReslutTypeVo(VisitorStatusEnum.Send_Email_Captcha_OK);
    }
}
