package com.zcw.cblog.user.service;

import com.zcw.cblog.common.to.UserEntityTo;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.user.entity.UserEntity;
import com.zcw.cblog.user.entity.UserRelationEntity;
import com.zcw.cblog.user.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description TODO:User类接口
 * @Author YuYu
 * @Date 2020-01-14 14:59
 * @Version 1.0
 */
public interface UserService {
    /**
     * 用户注册
     * @param username
     * @param password
     * @param request
     * @return
     */
    Integer addUser(String username, String password, HttpServletRequest request);

    /**
     * 用户信息查询
     * @param uid
     * @return
     */
    UserTo findUserById(Long uid);

    /**
     * 忘记密码----修改密码
     * @param username
     * @param password
     * @return
     */
    Integer modifyUserPassword(String username, String password);

    /**
     * 根据用户名 查询用户密码
     * @param username -- 用户邮箱登录的
     * @return
     */
    UserEntity findUserByName(String username);
    UserEntityTo findUserToByName(String username);

    /**
     * 更新用户基本信息
     * @param userInfoDto
     * @return
     */
    Integer modifyUserInfo(UserInfoVo userInfoDto, String id);

    /**
     * 修改用户头像
     * @param userDto
     * @return
     */
    Integer modifyUserInfo(UserTo userDto);

    /**
     * 更新登录时间
     * @param user
     * @return
     */
    Integer updateLastTime(UserEntity user);

    /**
     * 添加关系表
     * @param userRelation
     * @return
     */
    Integer addRelation(UserRelationEntity userRelation);

    /**
     * 修改关系表 --- 目前直接删除
     * @param userRelation
     * @return
     */
    Integer modifyRelation(UserRelationEntity userRelation);


    /**
     * 根据uid和rid查询是否有关系记录
     * @param userRelation
     * @return
     */
    UserRelationEntity findRelation(UserRelationEntity userRelation);


    /**
     * 查询粉丝 或偶像 列表 status区分
     * @param uid
     * @param status
     * @return
     */
    List<UserTo> findRelationList(Long uid, Integer status, Integer start);


    /**
     * 查询粉丝 或偶像 总数status区分
     * @param uid
     * @param status
     * @return
     */
    Integer findRelationTotal(Long uid, Integer status);

    /**
     * 通过id增加经验
     * @param uid
     * @param exp
     * @return
     */
    Integer addExpByUid(Long uid, Integer exp);

    List<UserTo> searchUserByKeyword(String keyword);
    Integer searchUserByKeywordNum(String keyword);
}
