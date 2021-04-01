package com.zcw.cblog.common.vo;

/**
 * @Description TODO:分页实体类
 */
public class PageVo {

    //当前页
    private Integer currentPage;
    //条数
    private Integer rows;
    //从哪条开始
    private Integer start;


    //查询类型--置顶
    private Integer topStatus;
    //查询类型--回复数
    private Integer commentNum;
    //查询类型--文章类型
    private Integer articleType;
    //通过aid查询评论列表
    private Integer aid;

    //需点赞用户的uid或查询关注人的动态uid
    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(Integer topStatus) {
        this.topStatus = topStatus;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    @Override
    public String toString() {
        return "PageVo{" +
                "currentPage=" + currentPage +
                ", rows=" + rows +
                ", start=" + start +
                ", topStatus=" + topStatus +
                ", commentNum=" + commentNum +
                ", articleType=" + articleType +
                ", aid=" + aid +
                ", uid=" + uid +
                '}';
    }
}
