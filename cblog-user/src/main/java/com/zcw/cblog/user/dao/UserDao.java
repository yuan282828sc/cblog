package com.zcw.cblog.user.dao;

import com.zcw.cblog.common.to.UserEntityTo;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.user.entity.UserEntity;
import com.zcw.cblog.user.entity.UserRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO:用户持久层
 * @Author YuYu
 * @Date 2020-01-14 12:38
 * @Version 1.0
 */
@Mapper
@Repository
public interface UserDao {
    /**
     * 用户注册
     * @param user
     * @return
     */
    Integer addUser(UserEntity user);
    /**
     * 用户信息查询
     * @param uid
     * @return
     */
    UserEntity findUserById(Long uid);
    /**
     * 根据用户名 查询用户密码
     * @param username -- 用户邮箱登录的
     * @return
     */
    UserEntity findUserByName(String username);
    UserEntityTo findUserToByName(String username);
    /**
     * 更新用户密码
     * @param user
     * @return
     */
    Integer modifyUserPassword(UserEntity user);
    /**
     * 更新用户基本信息
     * @param user
     * @return
     */
    Integer modifyUserInfo(UserEntity user);
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
     * 修改关系表 --目前是删除关注记录
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
     * 查询粉丝 或偶像 status区分
     * @param uid
     * @param status
     * @return
     */
    List<UserTo> findRelationList(@Param("uid") Long uid, @Param("status") Integer status, @Param("start") Integer start);
    /**
     * 查询粉丝 或偶像 总数status区分
     * @param uid
     * @param status
     * @return
     */
    Integer findRelationTotal(@Param("uid") Long uid, @Param("status") Integer status);

    /**
     * 通过id增加经验
     * @param uid
     * @param exp
     * @return
     */
    Integer addExpByUid(@Param("uid") Long uid, @Param("exp") Integer exp);

    List<UserTo> searchUserByKeyword(@Param("keyword") String keyword);

    Integer searchUserByKeywordNum(@Param("keyword") String keyword);
}
