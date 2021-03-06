package com.zcw.cblog.article.controller;

import com.zcw.cblog.article.entity.ArticleFavoriteEntity;
import com.zcw.cblog.article.feign.FileFeignService;
import com.zcw.cblog.article.service.ArticleCommentService;
import com.zcw.cblog.article.service.ArticleService;
import com.zcw.cblog.article.vo.ArticleDetailVo;
import com.zcw.cblog.article.vo.ArticleVo;
import com.zcw.cblog.article.vo.CommentVo;
import com.zcw.cblog.common.constant.TextType;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.common.utils.DateUtils;
import com.zcw.cblog.common.vo.FileVo;
import com.zcw.cblog.common.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO:所有人可以查看博客类相关
 */
@Controller
@RequestMapping("/article")
public class BasisController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleCommentService articleCommentService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private FileFeignService fileFeignService;

    /**
     * 查看博客列表
     * 看帖 精贴 公告
     * @param currentPage
     * @param request
     * @return
     */
    @GetMapping(value = {"/noReply/{currentPage}", "/index/{currentPage}", "/boutique/{currentPage}", "/announcement/{currentPage}"})
    public ModelAndView findBlogList(@PathVariable("currentPage") String currentPage, HttpServletRequest request) {
        //获取查询文章list的关键字
        String requestURI = request.getRequestURI();
        String keyword = requestURI.substring(requestURI.lastIndexOf("g") + 2, requestURI.lastIndexOf("/"));
        ModelAndView modelAndView = new ModelAndView();
        Integer pageNum = Integer.parseInt(currentPage);
        //如果请求值被恶意刷成负数则进行处理
        if (pageNum < 1) {
            pageNum = 1;
        }
        String time = DateUtils.date2String(new Date(), "MM-dd");
        //设置查询方式
        PageVo page = new PageVo();
        //查询条数
        page.setRows(10);
        //开始条数---默认首页从0开始
        page.setStart((pageNum - 1) * 10);
        //查询文章列表通过类型
        BasisController.setQueryWay(keyword, modelAndView, page);
        List<ArticleVo> articleList = articleService.findBlogList(page);
        List<ArticleVo> hotSpotArticleList = articleService.hotSpot();
        //总条数
        Integer total = articleService.findTotal(page);
        modelAndView.addObject("hotSpotArticleList", hotSpotArticleList);
        modelAndView.addObject("total", total);
        modelAndView.addObject("time", time);
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("articleList", articleList);
        return modelAndView;
    }

    /**
     * 关注人的帖子
     *
     * @param currentPage
     * @param request
     * @return
     */
    @GetMapping("/relation/{currentPage}")
    public ModelAndView findRelationBlogList(@PathVariable("currentPage") String currentPage, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blog/relation");
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        Integer pageNum = Integer.parseInt(currentPage);
        //如果请求值被恶意刷成负数则进行处理
        if (pageNum < 1) {
            pageNum = 1;
        }
        String time = DateUtils.date2String(new Date(), "MM-dd");
        //设置查询方式
        PageVo page = new PageVo();
        //查询条数
        page.setRows(10);
        //根据uid查询其关注人的动态
        page.setUid(loginUser.getUid());
        //开始条数---默认首页从0开始
        page.setStart((pageNum - 1) * 10);
        List<ArticleVo> articleList = articleService.findRelationBlogList(page);
        //总条数
        Integer total = articleService.findRelationBlogTotal(page);
        List<ArticleVo> hotSpotArticleList = articleService.hotSpot();
        modelAndView.addObject("hotSpotArticleList", hotSpotArticleList);
        modelAndView.addObject("hotSpotArticleList", hotSpotArticleList);
        modelAndView.addObject("total", total);
        modelAndView.addObject("time", time);
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("articleList", articleList);
        return modelAndView;
    }

    /**
     * 查看博客细节--包括评论列表
     *
     * @param aid
     * @return
     */
    @GetMapping(value = {"/detail/{aid}/page/{currentPage}", "/detail/{aid}"})
    public ModelAndView blogDetail(@PathVariable("aid") String aid
            , @PathVariable(value = "currentPage", required = false) String currentPage
            , HttpServletRequest request) {


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("blog/detail");
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        Integer id = Integer.valueOf(aid);

//        15秒防刷浏览量
//        用户ip加aid拼接成key
        String key = request.getRemoteAddr() + "_" + aid;
        //查询
        String result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            //点击一次链接加一个浏览量
            articleService.addBrowseNum(id);
            //存入十五秒，设置key
            redisTemplate.opsForValue().set(key, "mark", 15, TimeUnit.SECONDS);
        }

        //文章内容
        ArticleDetailVo blog = articleService.findBlogByAid(id);
        //最近热议
        List<ArticleVo> hotSpotArticleList = articleService.hotSpot();
        if (currentPage == null) {
            currentPage = "1";
        }
        Integer pageNum = Integer.parseInt(currentPage);
        //如果请求值被恶意刷成负数则进行处理
        if (pageNum < 1) {
            pageNum = 1;
        }
        //初始页面查询
        PageVo page = new PageVo();
        page.setAid(id);
        page.setStart((pageNum - 1) * 10);
        page.setRows(10);
        //判断是游客访问还是点赞用户来获取不同的评论列表
        List<CommentVo> commentList = null;
        //如果条数为1则收藏了博客
        Integer collectBlogTotal = null;
        if (loginUser != null) {
            //点赞者的评论列表
            Long uid = loginUser.getUid();
            page.setUid(uid);
            commentList = articleCommentService.findCommentByUid(page);
            //查询收藏记录
            ArticleFavoriteEntity collectBlog = new ArticleFavoriteEntity();
            collectBlog.setAid(id);
            collectBlog.setUid(uid);
            //如果为1则收藏了博客
            collectBlogTotal = articleService.findCollectBlog(collectBlog);
        } else {
            //游客的评论列表
            commentList = articleCommentService.findCommentByAid(page);
            collectBlogTotal = 0;
        }
        //评论列表
        //评论总条数
        Integer total = articleCommentService.findTotal(page);

        List<FileVo> fileVo = fileFeignService.getFilesByAid(id);


        modelAndView.addObject("total", total);
        modelAndView.addObject("collectBlogTotal", collectBlogTotal);
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("commentList", commentList);
        modelAndView.addObject("blog", blog);
        modelAndView.addObject("hotSpotArticleList", hotSpotArticleList);
        modelAndView.addObject("fileVos",fileVo);
        return modelAndView;
    }

    /**
     * 查询方式分类
     *
     * @param key
     * @param modelAndView
     * @param page
     */
    private static void setQueryWay(String key, ModelAndView modelAndView, PageVo page) {
        if (TextType.INDEX.equals(key)) {
            modelAndView.setViewName("blog/index");
            //查询普通文章
            page.setArticleType(0);
        } else if (TextType.NO_REPLY.equals(key)) {
            modelAndView.setViewName("blog/noReply");
            //查询方式--0回复
            page.setCommentNum(0);
        } else if (TextType.BOUTIQUE.equals(key)) {
            modelAndView.setViewName("blog/boutique");
            //查询精品贴
            page.setArticleType(1);
        } else if (TextType.ANNOUNCEMENT.equals(key)) {
            modelAndView.setViewName("blog/announcement");
            //查询公告
            page.setArticleType(2);
        } else {
            modelAndView.setViewName("blog/index");
            //查询普通文章--防止接口URI错误
            page.setArticleType(0);
        }
    }

}
