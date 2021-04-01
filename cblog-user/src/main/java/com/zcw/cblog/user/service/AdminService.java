package com.zcw.cblog.user.service;

/**
 * @Description TODO:管理员
 */
public interface AdminService {
    /**
     * 设置置顶
     * @param top
     * @param aid
     * @return
     */
    Integer setTopBlog(Integer top, Integer aid);

    /**
     * 设置精贴
     * @param article_type
     * @param aid
     * @return
     */
    Integer setBoutiqueBlog(Integer article_type, Integer aid);
}
