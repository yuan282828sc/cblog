package com.zcw.cblog.user.service.impl;

import com.zcw.cblog.user.dao.AdminDao;
import com.zcw.cblog.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description TODO:管理员权限
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public Integer setTopBlog(Integer top, Integer aid) {
        return adminDao.setTopBlog(top,aid);
    }

    @Override
    public Integer setBoutiqueBlog(Integer articleType, Integer aid) {
        return adminDao.setBoutiqueBlog(articleType,aid);
    }

}
