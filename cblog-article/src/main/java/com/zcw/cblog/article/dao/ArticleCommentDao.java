package com.zcw.cblog.article.dao;

import com.zcw.cblog.article.entity.ArticleCommentEntity;
import com.zcw.cblog.article.entity.ArticleCommentLikeEntity;
import com.zcw.cblog.article.vo.CommentVo;
import com.zcw.cblog.common.vo.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Description TODO:评论相关
 */
@Mapper
@Repository
public interface ArticleCommentDao {
    /**
     * 通过aid查询评论
     * @param page
     * @return
     */
    List<CommentVo> findCommentByAid(PageVo page);

    /**
     * 通过点赞人Uid查询它所看到的评论列表   主要用与展示用户的评论点赞状态
     * @param page
     * @return
     */
    List<CommentVo> findCommentByUid(PageVo page);

    /**
     *  通过类别查询总数--aid
     * @param page
     * @return
     */
    Integer findTotal(PageVo page);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    Integer addComment(ArticleCommentEntity comment);

    /**
     * 查询评论点赞记录数 --uid 和acid为依据 --返回条数
     * @param loveRecord
     * @return
     */
    Integer findLoveRecord(ArticleCommentLikeEntity loveRecord);

    /**
     * 添加点赞记录
     * @param loveRecord
     * @return
     */
    Integer addLoveRecord(ArticleCommentLikeEntity loveRecord);

    /**
     * 删除点赞记录 --uid 和acid为依据
     * @param loveRecord
     * @return
     */
    Integer delLoveRecord(ArticleCommentLikeEntity loveRecord);
    /**
     * 删除评论
     * @param acid
     * @return
     */
    Integer delComment(Integer acid);
}
