package com.zcw.cblog.file.dao;

import com.zcw.cblog.common.vo.FileVo;
import com.zcw.cblog.file.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chrisz
 * @date 2021/3/16 - 19:13
 */

@Mapper
@Repository
public interface FileDao {

    Integer uploadFiles( FileEntity fileEntity);

    List<FileVo> getFilesByAid(@Param("aid") Integer aid);

}
