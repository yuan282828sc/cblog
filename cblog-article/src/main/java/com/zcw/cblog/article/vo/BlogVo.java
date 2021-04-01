package com.zcw.cblog.article.vo;

import lombok.Data;

/**
 * @Description TODO:编写有关博客类
 */
@Data
public class BlogVo {
    //用户id
    private String uid;
    //文章id
    private Integer aid;
    //标题
    private String title;
    //内容含html
    private String content;
    //上传图片时使用
    private String file;
    //是否允许评论
    private String reviewStatus;
    //公开方式
    private String openProcedure;

}
