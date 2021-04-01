package com.zcw.cblog.article.controller;

import com.zcw.cblog.article.entity.ArticleCommentEntity;
import com.zcw.cblog.article.entity.ArticleCommentLikeEntity;
import com.zcw.cblog.article.feign.MessageFeignService;
import com.zcw.cblog.article.service.ArticleCommentService;
import com.zcw.cblog.article.service.ArticleService;
import com.zcw.cblog.article.vo.CommentVo;
import com.zcw.cblog.common.statusenum.BlogStatusEnum;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.common.vo.PageVo;
import com.zcw.cblog.common.vo.ReplyVo;
import com.zcw.cblog.common.vo.ReslutTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO:评论相关
 */
@Controller
@RequestMapping("/article/comment")
public class ArticleCommentController {
    @Autowired
    private ArticleCommentService articleCommentService;
    @Autowired
    private MessageFeignService messageFeignService;

    @Autowired
    private ArticleService articleService;

    /**
     * 评论文章
     *
     * @param
     * @return
     */
    @PostMapping("/review")
    @ResponseBody
    public ReslutTypeVo review(@RequestBody ReplyVo replyVo) {
        Long rid = replyVo.getRid();
        Long uid = replyVo.getUid();
        Long auid = replyVo.getAuid();
        ArticleCommentEntity comment = new ArticleCommentEntity();
        comment.setUid(uid);
        //trim() 方法用于删除字符串的头尾空白符。
        comment.setContent(replyVo.getContent().trim());
        comment.setAid(replyVo.getAid());

        Integer integer = null;
        try {
            integer = articleCommentService.addComment(comment);
            //更新文章排行权重。
            articleService.updateBlogTime(replyVo.getAid(), new Date());
        } catch (Exception e) {
            return new ReslutTypeVo(BlogStatusEnum.Review_Blog_Fail);
        }
        //写入数据库成功，然后发送消息提醒
        if (integer == 1) {
            //如果是同一人 则 不发消息
            if (uid.equals(auid) && rid == null || uid.equals(rid)) {
                return new ReslutTypeVo(BlogStatusEnum.Review_Blog_OK);
            } else {
                //发送消息
                messageFeignService.addMessage(replyVo);
            }
            return new ReslutTypeVo(BlogStatusEnum.Review_Blog_OK);
        }
        return new ReslutTypeVo(BlogStatusEnum.Review_Blog_Fail);
    }

    /**
     * 评论点赞
     *
     * @return
     */
    @PostMapping("/likeMark")
    @ResponseBody
    public ReslutTypeVo likeMark(@RequestParam("mark") String status,
                                  @RequestParam("id") String acid,
                                  @RequestParam("uid") String uid) {
        ArticleCommentLikeEntity loveRecord = new ArticleCommentLikeEntity();
        loveRecord.setCreateTime(new Date());
        loveRecord.setAcid(Integer.valueOf(acid));
        loveRecord.setStatus(Integer.valueOf(status));
        loveRecord.setUid(Long.valueOf(uid));
        //查询该用户该评论点赞数数 -- 防止出现数据异常
        Integer recordTotal = articleCommentService.findLoveRecord(loveRecord);
        if (loveRecord.getStatus() == 0 && recordTotal > 0) {
            Integer delLoveRecord = articleCommentService.delLoveRecord(loveRecord);
            if (delLoveRecord != 0) {
                //删除记录成功，取消点赞
                return new ReslutTypeVo(BlogStatusEnum.like_Comment_Fail);
            } else {
                //否则操作异常
                return new ReslutTypeVo(BlogStatusEnum.like_Comment_Exception);
            }
        } else if (loveRecord.getStatus() == 1 && recordTotal == 0) {
            Integer addLoveRecord = articleCommentService.addLoveRecord(loveRecord);
            if (addLoveRecord == 1) {
                //删除记录成功，取消点赞
                return new ReslutTypeVo(BlogStatusEnum.like_Comment_OK);
            } else {
                //否则操作异常
                return new ReslutTypeVo(BlogStatusEnum.like_Comment_Exception);
            }
        }
        //否则操作异常
        return new ReslutTypeVo(BlogStatusEnum.like_Comment_Exception);
    }

    /**
     * 查询评论---分页
     *
     * @param aid
     * @param currentPage
     * @return
     */
    @PostMapping("/{aid}/{currentPage}")
    @ResponseBody
    public Map<String, Object> findComment(@PathVariable("aid") String aid
            , @PathVariable(value = "currentPage") String currentPage
            , HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
        Map<String, Object> map = new HashMap<>();
        Integer id = Integer.valueOf(aid);
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
        page.setUid(loginUser.getUid());
        List<CommentVo> commentList = articleCommentService.findCommentByUid(page);
        Integer total = articleCommentService.findTotal(page);
        map.put("total", total);
        map.put("currentPage", pageNum);
        map.put("commentList", commentList);
        return map;
    }


    /**
     * 删除评论--删除点赞
     * @param acid
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public ReslutTypeVo delComment(@RequestParam("acid") Integer acid) {
        Integer integer = articleCommentService.delComment(acid);
        if (integer == 1) {
            //成功则返回ok否则直接异常处理
            return new ReslutTypeVo(BlogStatusEnum.del_Comment_OK);
        }
        return new ReslutTypeVo(BlogStatusEnum.del_Comment_Exception);
    }
}
