package com.zcw.cblog.common.vo;

/**
 * @Description TODO:评论相关字段
 */
public class ReplyVo {

    //文章id
    private Integer aid;
    //博主auid
    private Long auid;
    //发送用户id
    private Long uid;
    //接受用户id
    private Long rid;
    //评论内容
    private String content;

    @Override
    public String toString() {
        return "ReplyDto{" +
                "aid=" + aid +
                ", auid=" + auid +
                ", uid=" + uid +
                ", rid=" + rid +
                ", content='" + content + '\'' +
                '}';
    }

    public Long getAuid() {
        return auid;
    }

    public void setAuid(Long auid) {
        this.auid = auid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
