package com.zcw.cblog.user.controller;

import com.zcw.cblog.common.constant.TextType;
import com.zcw.cblog.common.statusenum.UserStatusEnum;
import com.zcw.cblog.common.to.ArticleTo;
import com.zcw.cblog.common.to.MessageTo;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.common.utils.DateUtils;
import com.zcw.cblog.common.vo.ReslutTypeVo;
import com.zcw.cblog.user.entity.UserEntity;
import com.zcw.cblog.user.entity.UserRelationEntity;
import com.zcw.cblog.user.feign.MessageFeignService;
import com.zcw.cblog.user.service.NginxService;
import com.zcw.cblog.user.service.UserService;
import com.zcw.cblog.common.to.FavoriteArticleTo;
import com.zcw.cblog.user.vo.MessageVo;
import com.zcw.cblog.user.vo.UserInfoVo;
import com.zcw.cblog.user.feign.ArticleFeignService;
import feign.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO:用户登录后的界面
 */
@Controller
//@RequestMapping("/user")
public class UserController {


    @Autowired
    private NginxService nginxService;
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleFeignService articleFeignService;

    @Autowired
    private MessageFeignService messageFeignService;


    /**
     * 更新最后登录时间
     * @param
     * @return
     */
    @PostMapping("/updateLastTime")
    @ResponseBody
    public Integer updateLastTime(@RequestBody UserEntity userEntity){
        return userService.updateLastTime(userEntity);

    }

    /**
     * 查询用户信息
     *
     * @param
     * @param
     * @return
     */
    @GetMapping("/findUserById")
    @ResponseBody
    public UserTo findUserById(@RequestParam("uid") Long uid) {
        UserTo userEntity = userService.findUserById(uid);
        return userEntity;
    }

    /**
     * 查询用户--邮箱登录的
     *
     * @param
     * @param
     * @return
     */
    @GetMapping("/findUserByName")
    public UserEntity findUserByName(@RequestParam("username") String username) {
        UserEntity userEntity = userService.findUserByName(username);
        return userEntity;
    }

    /**
     * 查询用户--信息用于修改展示
     *
     * @param uid
     * @param model
     * @return
     */
    @GetMapping("/set/{uid}")
    public String findUserById(@PathVariable("uid") String uid, Model model) {
        Long id = Long.parseLong(uid);
        UserTo userVo = userService.findUserById(id);
        model.addAttribute("UserInfo", userVo);
        return "user/set";
    }

    /**
     * 用户的所有帖子主页--以及收藏
     *
     * @param request
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/index");
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        Long uid = loginUser.getUid();
//最近十条---公开的
        List<ArticleTo> personalArticleList = articleFeignService.findBlogByUid(uid, 0, 0);
//       公开的文章总数
        Integer personalArticlesTotal = articleFeignService.findTotalByUid(uid, 0);
//最近十条---私密的
        List<ArticleTo> privacyArticlesList = articleFeignService.findBlogByUid(uid, 1, 0);
//       私密的文章总数
        Integer privacyArticlesTotal = articleFeignService.findTotalByUid(uid, 1);
// 最近十条----收藏的
        FavoriteArticleTo favoriteArticleTo = new FavoriteArticleTo();
        favoriteArticleTo.setUid(uid);
        List<ArticleTo> collectBlogList = articleFeignService.findCollectBlogList(uid, 0);
//     收藏的文章总数
        Integer collectArticlesTotal = articleFeignService.findCollectBlog(favoriteArticleTo);
        modelAndView.addObject("personalArticlesTotal", personalArticlesTotal);
        modelAndView.addObject("personalArticleList", personalArticleList);
        modelAndView.addObject("privacyArticlesTotal", privacyArticlesTotal);
        modelAndView.addObject("privacyArticleList", privacyArticlesList);
        modelAndView.addObject("collectArticlesTotal", collectArticlesTotal);
        modelAndView.addObject("collectBlogList", collectBlogList);
        return modelAndView;
    }


    /**
     * ajax分页查看用户帖子
     *
     * @param type
     * @param currentPage
     * @param request
     * @return
     */
    @PostMapping("/index/page")
    @ResponseBody
    public Map<String, Object> indexPage(@RequestParam("type") String type,
                                         @RequestParam("currentPage") String currentPage,
                                         HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        Long uid = loginUser.getUid();
        Integer curr = Integer.valueOf(currentPage);
        List<ArticleTo> articleDtoList = null;
        //根据不同类型进行分页查询
        if (type.equals(TextType.PERSONAL_ARTICLE)) {
            articleDtoList = articleFeignService.findBlogByUid(uid, 0, (curr - 1) * 10);
        } else if (TextType.PRIVACY_ARTICLE.equals(type)) {
            articleDtoList = articleFeignService.findBlogByUid(uid, 1, (curr - 1) * 10);
        } else if (TextType.Favorite_BLOG.equals(type)) {
            articleDtoList = articleFeignService.findCollectBlogList(uid, (curr - 1) * 10);
        }
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("articleDtoList", articleDtoList);
        return map;
    }

    /**
     * 修改用户头像
     *
     * @param userVo
     * @return
     */
    @PostMapping("/setavatar")
    @ResponseBody
    public Integer modifyAvatar(@RequestBody UserTo userVo) {

        Integer code = userService.modifyUserInfo(userVo);

        return code;
    }
    /**
     * 修改用户基本信息
     *
     * @param UserInfoVo
     * @return
     */
    @PostMapping("/set/{uid}")
    @ResponseBody
    public ReslutTypeVo modifyInfo(@RequestBody UserInfoVo UserInfoVo,
                                   @PathVariable("uid") String uid,
                                   HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        Integer integer = null;
        try {
            integer = userService.modifyUserInfo(UserInfoVo, uid);
        } catch (Exception e) {
            //特殊字段抛出异常
            return new ReslutTypeVo(UserStatusEnum.User_Info_Modify_Fail);
        }
        if (integer == null || !integer.equals(1)) {
            return new ReslutTypeVo(UserStatusEnum.User_Info_Modify_Fail);
        }
        //session中得数据得同步：比如昵称
        loginUser.setNickName(UserInfoVo.getNickname());
        session.setAttribute("loginUser", loginUser);
        return new ReslutTypeVo(UserStatusEnum.User_Info_Modify_OK);
    }

    /**
     * 上传图片方法
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/upload/articlePicture/", "/upload/avatar/{uid}"})
    @ResponseBody
    public Map<String, Object> uploadFile(HttpServletRequest request, HttpSession sessionRedis, @Param("file") MultipartFile file, @PathVariable(value = "uid",required = false) String uid) throws IOException {
        String res = DateUtils.date2String(new Date(), "yyyyMMddHHmmssSS");
//        //本地使用
//        String rootPath = "D:/JAVA/IDEA_Project/CBlog/cblog/cblogFile/";
//        HttpSession session = request.getSession();
//        UserTo loginUser1 = (UserTo) session.getAttribute("loginUser");
//        UserTo loginUser = (UserTo) sessionRedis.getAttribute("loginUser");
        //获取原始名称
        String originalFilename = file.getOriginalFilename();
        //新的文件名称用时间代替。
        String newFileName = res + originalFilename.substring(originalFilename.lastIndexOf("."));


        String requestUrl = request.getRequestURI();
        String way = requestUrl.substring(requestUrl.lastIndexOf("d") +2,requestUrl.lastIndexOf("/"));
//        判断是哪个接口uri访问的

        //nginx中头像的地址
        String fileUrl = "/static/cblog/images/"+way+"/"+newFileName;
        //nginx
        String p = "/static/cblog/images/"+way+"/";
        Integer result = nginxService.uploadPicture(file,newFileName,p);


//        //新文件绝对路径
//        File newFile = new File(rootPath + newFileName);
//        //判断目标文件所在的目录是否存在
//        if (!newFile.getParentFile().exists()) {
//            //如果目标文件所在的目录不存在，则创建父目录
//            newFile.getParentFile().mkdirs();
//        }
//        //将内存中的数据写入磁盘
//        file.transferTo(newFile);
//        //完整的url -- 为虚拟路径
//        String newFileName1 = "20.jpg";
//        String fileUrl = "/static/cblog/images/avatar/" + newFileName1;

        Map<String, Object> map = new HashMap<String, Object>(16);
        Map<String, Object> data = new HashMap<String, Object>(16);

        long u = 0l;
        if(uid!=null){
             u = Long.parseLong(uid);
        }
        //修改头像,判断是否修改
        Integer code = this.setAvatar(request,sessionRedis, newFileName,u);
        if (code == 1 && result ==1) {
            //上传成功
            map.put("code", 0);
            //提示消息
            map.put("msg", "上传成功");
        } else {
            //上传失败
            map.put("code", 0);
            //提示消息
            map.put("msg", "上传失败");
        }
        map.put("data", data);
        //图片url
        data.put("src", fileUrl);
        //图片名称，这个会显示在输入框里   可不写
        data.put("title", newFileName);
        return map;
    }
    /**
     * 修改用户头像
     * @param request
     * @param newFileName
     */
    private Integer setAvatar(HttpServletRequest request, HttpSession sessionRedis, String newFileName,Long uid) {
        String path = "avatar";
        String requestUrl = request.getRequestURI();
        String way = requestUrl.substring(requestUrl.lastIndexOf("d") +2,requestUrl.lastIndexOf("/"));
//        判断是哪个接口uri访问的
        if (!path.equals(way)) {
            //默认通过
            return 1;
        }

        sessionRedis.removeAttribute("loginUser");
        HttpSession session = request.getSession();
        UserTo loginUser1 = (UserTo) session.getAttribute("loginUser");

        Subject subject = SecurityUtils.getSubject();
        Session session2 = subject.getSession();
        UserTo loginUser2 = (UserTo) session2.getAttribute("loginUser");

        UserTo userById = userService.findUserById(uid);
//http://192.168.60.137/static/cblog/images/avatar/1.jpg
        userById.setAvatar("/static/cblog/images/avatar/" + newFileName);
        UserTo loginUser = (UserTo) sessionRedis.getAttribute("loginUser");

        //1 为修改成功  其他为失败
        Integer code = userService.modifyUserInfo(userById);
        //如果成功的话  新的头像路径数据回写
        if (code == 1 ){
            sessionRedis.setAttribute("loginUser",userById);
        }
        return code;
    }

    /**
     * 查看个人消息
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/message", "/message/{currentPage}"})
    public ModelAndView message(HttpServletRequest request,
                                @PathVariable(value = "currentPage", required = false) String currentPage) {
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
//        移除新消息提示
        session.removeAttribute("newMessageTotal");
        Long uid = loginUser.getUid();
        Integer pageNum = 1;
        if (currentPage != null) {
            pageNum = Integer.valueOf(currentPage);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/message");
        //查询最近回复 接受方
        List<MessageVo> messageList = messageFeignService.findMessageByRid(uid, (pageNum - 1) * 5);
        Integer total = messageFeignService.findTotalByRid(uid);
        modelAndView.addObject("messageList", messageList);
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("messageTotal", total);
        return modelAndView;
    }

    /**
     * 设置为已读
     *
     * @param request
     * @param mid
     * @return
     */
    @PostMapping(value = {"/message/readMessage", "/message/readAllMessage"})
    @ResponseBody
    public ReslutTypeVo readMessage(HttpServletRequest request,
                                    @RequestParam(value = "mid", required = false) Integer mid) {
        MessageTo message = new MessageTo();
        Integer total = null;
        //根据mid判断是设置所有还是一个
        if (mid == null) {
            HttpSession session = request.getSession();
            UserTo loginUser = (UserTo) session.getAttribute("loginUser");
            Long uid = loginUser.getUid();
            //接收方
            message.setRid(uid);
            total = messageFeignService.setReadStatus(message);
        } else {
            message.setId(mid);
            total = messageFeignService.setReadStatus(message);
        }
        if (total == 0) {
            return new ReslutTypeVo(UserStatusEnum.Message_Read_Fail);
        }
        return new ReslutTypeVo(UserStatusEnum.Message_Read_OK);
    }

    /**
     * 主页发送消息
     *
     * @return
     */
    @PostMapping(value = {"/message/send"})
    @ResponseBody
    public ReslutTypeVo sendMessage(@RequestParam("uid") Long uid,
                                    @RequestParam("rid") Long rid,
                                    @RequestParam("content") String content) {
        MessageTo message = new MessageTo();
        message.setRid(rid);
        message.setUid(uid);
        message.setContent(content);
        //状态2 为私信
        message.setStatus(2);
        Integer integer = null;
        try {
            integer = messageFeignService.sendMessage(message);
            if (integer == 1) {
                return new ReslutTypeVo(UserStatusEnum.Message_Send_OK);
            }
        } catch (Exception e) {
            return new ReslutTypeVo(UserStatusEnum.Message_Send_Fail);
        }
        return new ReslutTypeVo(UserStatusEnum.Message_Send_Fail);
    }

    /**
     * 主页改变关系
     *
     * @param uid
     * @param rid
     * @return
     */
    @PostMapping(value = {"/home/modifyRelation"})
    @ResponseBody
    public ReslutTypeVo modifyRelation(@RequestParam("uid") Long uid,
                                       @RequestParam("rid") Long rid,
                                       @RequestParam("status") Integer status) {
        UserRelationEntity userRelation = new UserRelationEntity();
        userRelation.setUid(uid);
        userRelation.setRid(rid);
        userRelation.setStatus(status);
        Integer integer = null;
        try {
            //添加关注
            if (status == 1) {
                integer = userService.addRelation(userRelation);
                if (integer == 1) {
                    return new ReslutTypeVo(UserStatusEnum.Add_Relation_OK);
                }
            } else {
                //取消关注
                integer = userService.modifyRelation(userRelation);
                if (integer == 1) {
                    return new ReslutTypeVo(UserStatusEnum.Modify_Relation_OK);
                }
            }
        } catch (Exception e) {
            return new ReslutTypeVo(UserStatusEnum.Relation_Exception);
        }
        return new ReslutTypeVo(UserStatusEnum.Relation_Exception);
    }

    /**
     * 查看主页
     *
     * @param uid
     * @return
     */
    @GetMapping("/home/{uid}")
    public ModelAndView findHomeById(@PathVariable("uid") String uid,
                                     HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/home");
        Long id = Long.parseLong(uid);
        //判断是否与登录用户有关系
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        if (loginUser != null) {
            UserRelationEntity userRelation = new UserRelationEntity();
            userRelation.setUid(loginUser.getUid());
            userRelation.setRid(id);
            UserRelationEntity relation = userService.findRelation(userRelation);
            modelAndView.addObject("relation", relation);
        }
        UserTo userDto = userService.findUserById(id);
        String time = DateUtils.date2String(new Date(), "yyyy-MM-dd");
        List<ArticleTo> articleList = articleFeignService.findBlogByUid(id, 0, 0);
        //查询最近回复 发送方
        List<MessageVo> messageList = messageFeignService.findMessageByUid(id);
        modelAndView.addObject("messageList", messageList);
        modelAndView.addObject("time", time);
        modelAndView.addObject("UserInfo", userDto);
        modelAndView.addObject("articleList", articleList);
        return modelAndView;
    }



    @PostMapping("/relation/page")
    @ResponseBody
    public Map<String, Object> relationPage(@RequestParam("type") String type,
                                            @RequestParam("currentPage") String currentPage,
                                            HttpServletRequest request){
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        Long uid = loginUser.getUid();
        int curr = Integer.parseInt(currentPage);
        List<UserTo> userInfoList = null;
        //根据不同类型进行分页查询
        if (TextType.IDOL_PAGE.equals(type)) {
            userInfoList = userService.findRelationList(uid, 0, (curr - 1) * 10);
        } else if (TextType.FANS_PAGE.equals(type)) {
            userInfoList = userService.findRelationList(uid, 1, (curr - 1) * 10);
        }
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("userInfoList", userInfoList);
        return map;
    }

    /**
     * 用户查看好友关系列表
     *
     * @return
     */
    @GetMapping("/relation")
    public ModelAndView userRelation(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        Long uid = loginUser.getUid();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/relation");
        //查询关注人列表
        List<UserTo> idolList = userService.findRelationList(uid, 0, 0);
        Integer idolTotal = userService.findRelationTotal(uid, 0);
        //查询粉丝列表
        List<UserTo> fansList = userService.findRelationList(uid, 1, 0);
        Integer fansTotal = userService.findRelationTotal(uid, 1);
        modelAndView.addObject("idolList", idolList);
        modelAndView.addObject("idolTotal",idolTotal);
        modelAndView.addObject("fansList", fansList);
        modelAndView.addObject("fansTotal",fansTotal);
        return modelAndView;
    }

    /**
     * 根据关键字查用户
     * @param
     * @return
     */
    @GetMapping("/searchUserByKeyword")
    @ResponseBody
    List<UserTo> searchUserByKeyword(@RequestParam ("keyword") String keyword){

        List<UserTo> userTos = userService.searchUserByKeyword(keyword);
        return userTos;
    }

    /**
     * 根据关键字查用户数量
     * @param
     * @return
     */
    @GetMapping("/searchUserByKeywordNum")
    @ResponseBody
    Integer searchUserByKeywordNum(@RequestParam ("keyword") String keyword){

        Integer userNum = userService.searchUserByKeywordNum(keyword);
        return userNum;
    }
}
