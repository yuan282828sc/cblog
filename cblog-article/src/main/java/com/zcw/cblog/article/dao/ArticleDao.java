package com.zcw.cblog.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcw.cblog.article.entity.ArticleEntity;
import com.zcw.cblog.article.entity.ArticleFavoriteEntity;
import com.zcw.cblog.article.vo.ArticleDetailVo;
import com.zcw.cblog.article.vo.ArticleVo;
import com.zcw.cblog.common.to.ArticleTo;
import com.zcw.cblog.common.vo.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO:博客相关
 */
@Mapper
public interface ArticleDao extends BaseMapper<ArticleEntity> {

    /**
     *添加一条博客
     * @return
     */
    Integer addBlog(ArticleEntity blog);

    /**
     * 查询用户自己发表的总数
     * @param uid
     * @return
     */
    Integer findTotalByUid(@Param("uid") Long uid, @Param("open_procedure") Integer open_procedure);

    /**
     * 初始化页面文章
     * @return
     */
    List<ArticleVo> homePageBlog(PageVo pageVo);

    /**
     * 分类查询博客列表
     * @param page
     * @return
     */
    List<ArticleVo> findBlogList(PageVo page);

    /**
     *  通过类别查询总数
     * @param page
     * @return
     */
    Integer findTotal(PageVo page);

    /**
     * 查询被关注人的博客列表
     * @param page
     * @return
     */
    List<ArticleVo> findRelationBlogList(PageVo page);

    /**
     * 查询被关注人的博客总数
     */
    Integer findRelationBlogTotal(PageVo page);

    /**
     * 最近七天热点动态
     * @return
     */
    List<ArticleVo> hotSpot();

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
     * 通过uid和类别查询文章分页
     * @param uid
     * @return
     */
    List<ArticleTo> findBlogByUid(@Param("uid") Long uid, @Param("open_procedure") Integer open_procedure, @Param("start") Integer start);

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
    List<ArticleTo> findCollectBlogList(@Param("uid") Long uid, @Param("start") Integer start);


    /**
     * 更新时间权重
     * @param aid
     * @param updateTime
     * @return
     */
    Integer updateBlogTime(@Param("aid") Integer aid, @Param("updateTime") Date updateTime);

    List<ArticleTo> searchBlogListByKeyword(@Param("keyword") String keyword);

    Integer searchBlogListByKeywordNum(@Param("keyword") String keyword);

    int insertReturnAid(@Param("articleEntity") ArticleEntity articleEntity);

    Integer updateArticle(ArticleEntity blog);
}
