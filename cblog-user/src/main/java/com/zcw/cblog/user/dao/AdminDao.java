package com.zcw.cblog.user.dao;



import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description TODO:管理员权限
 */
@Mapper
@Repository
public interface AdminDao {
    /**
     * 设置置顶
     *
     * @param top
     * @param aid
     * @return
     */
    Integer setTopBlog(@Param("top") Integer top, @Param("aid") Integer aid);

    /**
     * 设置精贴
     *
     * @param article_type
     * @param aid
     * @return
     */
    Integer setBoutiqueBlog(@Param("article_type") Integer article_type, @Param("aid") Integer aid);

}
