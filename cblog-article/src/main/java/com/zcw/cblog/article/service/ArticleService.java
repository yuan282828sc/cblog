package com.zcw.cblog.article.service;


import com.zcw.cblog.article.entity.ArticleEntity;
import com.zcw.cblog.article.entity.ArticleFavoriteEntity;
import com.zcw.cblog.article.vo.ArticleDetailVo;
import com.zcw.cblog.article.vo.ArticleVo;
import com.zcw.cblog.article.vo.BlogVo;
import com.zcw.cblog.common.to.ArticleTo;
import com.zcw.cblog.common.vo.PageVo;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO:博客相关
 */
public interface ArticleService {
    //添加博客
    Integer addBlog(BlogVo blogDto);

    //社区首页文章
    List<ArticleVo> homePageBlog(PageVo page);

    /**
     * 分类查询博客列表
     * @param page
     * @return
     */
    List<ArticleVo> findBlogList(PageVo page);

    /**
     * 检索服务
     * @param
     * @return
     */
    List<ArticleTo> searchBlogListByKeyword(String keyword);

    Integer searchBlogListByKeywordNum(String keyword);
    //通过类别查询总数
    Integer findTotal(PageVo page);

    /**
     * 查询被关注人的博客列表
     * @param page
     * @return
     */
    List<ArticleVo> findRelationBlogList(PageVo page);


    //查询被关注人的博客总数
    Integer findRelationBlogTotal(PageVo page);

    /**
     * 最近七天热点动态
     * @return
     */
    List<ArticleVo> hotSpot();
    /**
     * 通过uid和类别查询文章
     * @param uid
     * @return
     */
    List<ArticleTo> findBlogByUid(Long uid, Integer open_procedure, Integer start);

    /**
     * 查询博客细节通过aid
     * @param aid
     * @return
     */
    ArticleDetailVo findBlogByAid(Integer aid);

    /**
     * 添加浏览量
     *  @param aid
     * @return
     */
    Integer addBrowseNum(Integer aid);

    /**
     * 查询用户自己发表的总数
     * @param uid
     * @return
     */
    Integer findTotalByUid(Long uid, Integer open_procedure);

    /**
     * 删除文章
     * @param aid
     * @return
     */
    Integer delBlog(Integer aid);

    /**
     * 添加 收藏博客记录
     * @param
     * @return
     */
    Integer addCollectBlog(ArticleFavoriteEntity favoriteBlog);

    /**
     * 删除 收藏博客记录
     * @param
     * @return
     */
    Integer delCollectBlog(ArticleFavoriteEntity favoriteBlog);

    /**
     * 查询收藏记录数 --uid和aid为依据 --返回条数
     * @param
     * @return
     */
    Integer findCollectBlog(ArticleFavoriteEntity favoriteBlog);

    /**
     * 查询收藏文章及其分页
     * @param uid
     * @param start
     * @return
     */
    List<ArticleTo> findCollectBlogList(Long uid, Integer start);

    /**
     * 更新时间权重
     * @param aid
     * @param updateTime
     * @return
     */
    Integer updateBlogTime(Integer aid, Date updateTime);

    int insertReturnAid(ArticleEntity articleEntity);

    /**
     * 发博客其实是更新操作
     * @param blogDto
     * @return
     */
    Integer updateArticle(BlogVo blogDto);
}
