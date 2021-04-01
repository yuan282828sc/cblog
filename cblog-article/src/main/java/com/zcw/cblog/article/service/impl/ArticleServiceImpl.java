package com.zcw.cblog.article.service.impl;

import com.zcw.cblog.article.dao.ArticleDao;
import com.zcw.cblog.article.entity.ArticleEntity;
import com.zcw.cblog.article.entity.ArticleFavoriteEntity;
import com.zcw.cblog.article.service.ArticleService;
import com.zcw.cblog.article.vo.ArticleDetailVo;
import com.zcw.cblog.article.vo.ArticleVo;
import com.zcw.cblog.article.vo.BlogVo;
import com.zcw.cblog.common.to.ArticleTo;
import com.zcw.cblog.common.utils.RegExUtils;
import com.zcw.cblog.common.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @Description TODO:博客实现类
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao blogDao;



    @Override
    public List<ArticleVo> homePageBlog(PageVo page) {
        return blogDao.homePageBlog(page);
    }

    @Override
    public List<ArticleVo> findBlogList(PageVo page) {
        return blogDao.findBlogList(page);
    }
    @Override
    public List<ArticleTo> searchBlogListByKeyword(String keyword) {
        return blogDao.searchBlogListByKeyword(keyword);
    }

    @Override
    public Integer searchBlogListByKeywordNum(String keyword) {
        Integer articleNum = blogDao.searchBlogListByKeywordNum(keyword);
        return articleNum;
    }

    @Override
    public Integer findTotal(PageVo page) {
        return blogDao.findTotal(page);
    }

    @Override
    public List<ArticleVo> findRelationBlogList(PageVo page) {
        return blogDao.findRelationBlogList(page);
    }

    @Override
    public Integer findRelationBlogTotal(PageVo page) {
        return blogDao.findRelationBlogTotal(page);
    }

    @Override
    public List<ArticleVo> hotSpot() {
        return blogDao.hotSpot();
    }

    @Override
    public List<ArticleTo> findBlogByUid(Long uid, Integer open_procedure, Integer start) {
        return blogDao.findBlogByUid(uid,open_procedure,start);
    }

    @Override
    public ArticleDetailVo findBlogByAid(Integer aid) {
        return blogDao.findBlogByAid(aid);
    }

    @Override
    public Integer addBrowseNum(Integer aid) {
        return blogDao.addBrowseNum(aid);
    }

    @Override
    public Integer findTotalByUid(Long uid, Integer open_procedure) {
        return blogDao.findTotalByUid(uid,open_procedure);
    }

    @Override
    public Integer delBlog(Integer aid) {
        return blogDao.delBlog(aid);
    }
    @Override
    public Integer addCollectBlog(ArticleFavoriteEntity collectBlog) {
        return blogDao.addCollectBlog(collectBlog);
    }
    @Override
    public Integer delCollectBlog(ArticleFavoriteEntity collectBlog) {
        return blogDao.delCollectBlog(collectBlog);
    }
    @Override
    public Integer findCollectBlog(ArticleFavoriteEntity collectBlog) {
        return blogDao.findCollectBlog(collectBlog);
    }

    @Override
    public List<ArticleTo> findCollectBlogList(Long uid, Integer start) {
        return blogDao.findCollectBlogList(uid,start);
    }

    @Override
    public Integer updateBlogTime(Integer aid, Date updateTime) {
        return blogDao.updateBlogTime(aid,updateTime);
    }

    @Override
    public int insertReturnAid(ArticleEntity articleEntity) {
        return blogDao.insertReturnAid(articleEntity);
    }

    @Override
    public Integer addBlog(BlogVo blogDto) {
        ArticleEntity blog = new ArticleEntity();
        blog.setUid(Long.parseLong(blogDto.getUid()));
        blog.setTitle(blogDto.getTitle());
        blog.setContent(blogDto.getContent());
        blog.setTempFile(RegExUtils.regExFileName(blog.getContent()).toString());
        blog.setReviewStatus(Integer.valueOf(blogDto.getReviewStatus()));
        blog.setOpenProcedure(Integer.valueOf(blogDto.getOpenProcedure()));
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        return blogDao.addBlog(blog);
    }

    @Override
    public Integer updateArticle(BlogVo blogDto) {

        ArticleEntity blog = new ArticleEntity();
        blog.setAid(blogDto.getAid());
        blog.setUid(Long.parseLong(blogDto.getUid()));
        blog.setTitle(blogDto.getTitle());
        blog.setContent(blogDto.getContent());
        blog.setTempFile(RegExUtils.regExFileName(blog.getContent()).toString());
        blog.setReviewStatus(Integer.valueOf(blogDto.getReviewStatus()));
        blog.setOpenProcedure(Integer.valueOf(blogDto.getOpenProcedure()));
        blog.setUpdateTime(new Date());
        return blogDao.updateArticle(blog);
    }
}
