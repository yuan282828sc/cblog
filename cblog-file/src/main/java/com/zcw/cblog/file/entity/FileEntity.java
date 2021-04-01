package com.zcw.cblog.file.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO:文章实体类
 */
@Data
public class FileEntity {
    //文章id
    private Integer fid;
    //标题
    private String fileName;
    //用户id
    private Long uid;
    //文章id
    private Long aid;
    //文件大小
    private String fileSize;
    //创建时间
    private Date uploadTime;
    //浏览量
    private Integer downloadNum;
    //内容
    private String path;
    //文章状态
    private Integer status;
    //是否置顶
    private Integer top;
    //文章类型  0：普通帖子，1：精贴  2：公告
    private Integer fileType;
    //公开方式  0：公开，1：私密  2；朋友可见
    private Integer openProcedure;
    //是否允许评论
    private Integer downloadStatus;
    //临时文件名-- 数组
    private String tempFile;

    //时间权重  用于排行显示
    private Date updateTime;


}
