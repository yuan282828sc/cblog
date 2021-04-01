package com.zcw.cblog.article.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcw.cblog.article.entity.ArticleEntity;
import com.zcw.cblog.article.entity.ArticleFavoriteEntity;
import com.zcw.cblog.article.feign.UserFeignService;
import com.zcw.cblog.article.service.ArticleService;
import com.zcw.cblog.article.service.NginxService;
import com.zcw.cblog.article.vo.ArticleVo;
import com.zcw.cblog.article.vo.BlogVo;
import com.zcw.cblog.article.vo.UserInfoTo;
import com.zcw.cblog.common.statusenum.BlogStatusEnum;
import com.zcw.cblog.common.to.ArticleTo;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.common.utils.DateUtils;
import com.zcw.cblog.common.vo.PageVo;
import com.zcw.cblog.common.vo.ReslutTypeVo;
import feign.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO:博客相关--需要用户权限
 */
@Controller
@Slf4j
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserFeignService userFeignService;



    @Autowired
    private NginxService nginxService;
//    /**
//     * 可上传图片、视频，只需在nginx配置中配置可识别的后缀
//     */
//    @RequestMapping("/upload/avatar")
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

    /**
     * 最近十条---公开的
     * @return
     */
    @GetMapping("/findBlogByUid")
    @ResponseBody
    public List<ArticleTo> findBlogByUid(@RequestParam("uid")Long uid,
                                         @RequestParam("open_procedure")Integer open_procedure,
                                         @RequestParam("start")Integer start) {
        List<ArticleTo> personalArticleList = articleService.findBlogByUid(uid, open_procedure, start);
        return personalArticleList;
    }

    /**
     * 公开的文章总数
     * @return
     */
    @GetMapping("/findTotalByUid")
    @ResponseBody
    public Integer findTotalByUid(@RequestParam("uid")Long uid, @RequestParam("open_procedure")Integer open_procedure) {
        Integer personalArticlesTotal = articleService.findTotalByUid(uid, open_procedure);
        return personalArticlesTotal;
    }

    /**
     * 最近十条----收藏的
     * @return
     */
    @GetMapping("/findCollectBlogList")
    @ResponseBody
    public List<ArticleTo> findCollectBlogList(@RequestParam("uid")Long uid, @RequestParam("start")Integer start) {
        List<ArticleTo> collectBlogList = articleService.findCollectBlogList(uid, start);
        return collectBlogList;
    }

    /**
     * 收藏的文章总数
     * @return
     */
    @PostMapping("/findCollectBlog")
    @ResponseBody
    public Integer findCollectBlog(@RequestBody ArticleFavoriteEntity favoriteBlogEntity) {
        Integer collectArticlesTotal = articleService.findCollectBlog(favoriteBlogEntity);
        return collectArticlesTotal;
    }

    @GetMapping("/add")
    public String addBlog(Model model,HttpSession session) {
        ArticleEntity articleEntity = new ArticleEntity();
        int count = articleService.insertReturnAid(articleEntity);
        Integer aid = articleEntity.getAid();
        session.setAttribute("aid",aid);
        model.addAttribute("aid",aid);
        return "blog/add";
    }

    /**
     * 删除帖子所有相关
     * @param aid
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public ReslutTypeVo delBlog(@RequestParam("aid")Integer aid){
        Integer integer = articleService.delBlog(aid);
        if (integer == 1) {
            return new ReslutTypeVo(BlogStatusEnum.del_Blog_OK);
        }
        return new ReslutTypeVo(BlogStatusEnum.del_Blog_Exception);
    }
    /**
     * 文章收藏
     * @param status
     * @param aid
     * @param uid
     * @return
     */
    @PostMapping("/collect")
    @ResponseBody
    public ReslutTypeVo collectBlog(@RequestParam("mark") String status,
                                     @RequestParam("aid") String aid,
                                     @RequestParam("uid") String uid){
        ArticleFavoriteEntity collectBlog = new ArticleFavoriteEntity();
        collectBlog.setCreateTime(new Date());
        collectBlog.setAid(Integer.valueOf(aid));
        collectBlog.setStatus(Integer.valueOf(status));
        collectBlog.setUid(Long.valueOf(uid));
        //查询该用户收藏 -- 防止出现数据异常
        Integer collectBlogTotal = articleService.findCollectBlog(collectBlog);
        if (collectBlog.getStatus() == 0 && collectBlogTotal > 0) {
            Integer delCollectBlog = articleService.delCollectBlog(collectBlog);
            if (delCollectBlog != 0) {
                //删除记录成功，取消收藏
                return new ReslutTypeVo(BlogStatusEnum.collect_Blog_Fail);
            } else {
                //否则操作异常
                return new ReslutTypeVo(BlogStatusEnum.collect_Blog_Exception);
            }
        } else if (collectBlog.getStatus() == 1 && collectBlogTotal == 0) {
            Integer addCollectBlog = articleService.addCollectBlog(collectBlog);
            if (addCollectBlog == 1) {
                //删除记录成功，取消收藏
                return new ReslutTypeVo(BlogStatusEnum.collect_Blog_OK);
            } else {
                //否则操作异常
                return new ReslutTypeVo(BlogStatusEnum.collect_Blog_Exception);
            }
        }
        //否则操作异常
        return new ReslutTypeVo(BlogStatusEnum.collect_Blog_Exception);
    }
    /**
     * 发布文章
     *
     * @param blogDto
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ReslutTypeVo addBlog(@RequestBody BlogVo blogDto) {
        Integer integer = null;
        try {
            //integer = articleService.addBlog(blogDto);
            integer = articleService.updateArticle(blogDto);
        } catch (Exception e) {
            return new ReslutTypeVo(BlogStatusEnum.Blog_Publish_Fail);
        }
        if (integer == 1) {
            return new ReslutTypeVo(BlogStatusEnum.Blog_Publish_OK);
        }
        return new ReslutTypeVo(BlogStatusEnum.Blog_Publish_Fail);
    }
    /**
     * 上传图片方法
     *
     * @param file
     * @return
     * @throws Exception
     */
//    @RequestMapping(value = {"/upload", "/upload/avatar/{uid}"})
//    @ResponseBody
//    public Map<String, Object> uploadFile(HttpServletRequest request,HttpSession sessionRedis, @Param("file") MultipartFile file, @PathVariable(value = "uid",required = false) String uid) throws IOException {
//        String res = DateUtils.date2String(new Date(), "yyyyMMddHHmmssSS");
////        //本地使用
////        String rootPath = "D:/JAVA/IDEA_Project/CBlog/cblog/cblogFile/";
//
//
//        HttpSession session = request.getSession();
//        UserTo loginUser1 = (UserTo) session.getAttribute("loginUser");
//        UserTo loginUser = (UserTo) sessionRedis.getAttribute("loginUser");
//        //获取原始名称
//        String originalFilename = file.getOriginalFilename();
//        //新的文件名称用时间代替。
//        String newFileName = res + originalFilename.substring(originalFilename.lastIndexOf("."));
//        //nginx中头像的地址
//        String fileUrl = "/static/cblog/images/avatar/"+newFileName;
//        //nginx
//        Integer result = nginxService.uploadPicture(file,newFileName);
//
//
////        //新文件绝对路径
////        File newFile = new File(rootPath + newFileName);
////        //判断目标文件所在的目录是否存在
////        if (!newFile.getParentFile().exists()) {
////            //如果目标文件所在的目录不存在，则创建父目录
////            newFile.getParentFile().mkdirs();
////        }
////        //将内存中的数据写入磁盘
////        file.transferTo(newFile);
////        //完整的url -- 为虚拟路径
////        String newFileName1 = "20.jpg";
////        String fileUrl = "/static/cblog/images/avatar/" + newFileName1;
//
//        Map<String, Object> map = new HashMap<String, Object>(16);
//        Map<String, Object> data = new HashMap<String, Object>(16);
//
//        long u = Long.parseLong(uid);
//        //修改头像,判断是否修改
//        Integer code = this.setAvatar(request,sessionRedis, newFileName,u);
//        if (code == 1 && result ==1) {
//            //上传成功
//            map.put("code", 1);
//            //提示消息
//            map.put("msg", "上传成功");
//        } else {
//            //上传失败
//            map.put("code", 0);
//            //提示消息
//            map.put("msg", "上传失败");
//        }
//        map.put("data", data);
//        //图片url
//        data.put("src", fileUrl);
//        //图片名称，这个会显示在输入框里   可不写
//        data.put("title", newFileName);
//        return map;
//    }
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
            return 0;
        }

        sessionRedis.removeAttribute("loginUser");
        HttpSession session = request.getSession();
        UserTo loginUser1 = (UserTo) session.getAttribute("loginUser");

        Subject subject = SecurityUtils.getSubject();
        Session session2 = subject.getSession();
        UserTo loginUser2 = (UserTo) session2.getAttribute("loginUser");

        UserTo userById = userFeignService.findUserById(uid);
//http://192.168.60.137/static/cblog/images/avatar/1.jpg
        userById.setAvatar("/static/cblog/images/avatar/" + newFileName);
        UserTo loginUser = (UserTo) sessionRedis.getAttribute("loginUser");

        //1 为修改成功  其他为失败
        Integer code = userFeignService.modifyAvatar(userById);
        //如果成功的话  新的头像路径数据回写
        if (code == 1 ){
            sessionRedis.setAttribute("loginUser",userById);
        }
        return code;
    }

    /**
     * 检索服务-搜索博客文章
     */
    @GetMapping("/searchBlogListByKeyword")
    @ResponseBody
    public List<ArticleTo> searchBlogListByKeyword(@RequestParam("keyword") String keyword){


        List<ArticleTo> articleVos = articleService.searchBlogListByKeyword(keyword);
        return articleVos;
    }

    @GetMapping("/searchBlogListByKeywordNum")
    @ResponseBody
    Integer searchBlogListByKeywordNum(@RequestParam("keyword") String keyword){

        Integer articleNum = articleService.searchBlogListByKeywordNum(keyword);
        return articleNum;
    }
}
