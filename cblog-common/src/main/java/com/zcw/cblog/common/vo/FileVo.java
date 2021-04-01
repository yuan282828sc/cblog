package com.zcw.cblog.common.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Chrisz
 * @date 2021/3/18 - 15:55
 */
@Data
public class FileVo {

    //标题
    private String fileName;
    //文件大小
    private String fileSize;
    //创建时间
    private Date uploadTime;
}
