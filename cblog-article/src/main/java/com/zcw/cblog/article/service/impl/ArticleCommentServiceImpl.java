package com.zcw.cblog.article.service.impl;

import com.zcw.cblog.article.dao.ArticleCommentDao;
import com.zcw.cblog.article.entity.ArticleCommentEntity;
import com.zcw.cblog.article.entity.ArticleCommentLikeEntity;
import com.zcw.cblog.article.service.ArticleCommentService;
import com.zcw.cblog.article.vo.CommentVo;
import com.zcw.cblog.common.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO:
 */
@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {

    @Autowired
    public ArticleCommentDao commentMapper;

    @Override
    public List<CommentVo> findCommentByAid(PageVo page) {
        return commentMapper.findCommentByAid(page);
    }

    @Override
    public List<CommentVo> findCommentByUid(PageVo page) {
        return commentMapper.findCommentByUid(page);
    }

    @Override
    public Integer findTotal(PageVo page) {
        return commentMapper.findTotal(page);
    }

    @Override
    public Integer addComment(ArticleCommentEntity comment) {
        comment.setCreateTime(new Date());
        return commentMapper.addComment(comment);
    }

    @Override
    public Integer findLoveRecord(ArticleCommentLikeEntity loveRecord) {
        return commentMapper.findLoveRecord(loveRecord);
    }

    @Override
    public Integer addLoveRecord(ArticleCommentLikeEntity loveRecord) {
        return commentMapper.addLoveRecord(loveRecord);
    }

    @Override
    public Integer delLoveRecord(ArticleCommentLikeEntity loveRecord) {
        return commentMapper.delLoveRecord(loveRecord);
    }

    @Override
    public Integer delComment(Integer acid) {
        return commentMapper.delComment(acid);
    }
}
