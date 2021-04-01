package com.zcw.cblog.user.service.impl;

import com.zcw.cblog.common.statusenum.AccountStatusEnum;
import com.zcw.cblog.common.to.UserEntityTo;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.common.utils.AddressByIpUtils;
import com.zcw.cblog.common.utils.DateUtils;
import com.zcw.cblog.common.utils.SaltUtils;
import com.zcw.cblog.common.utils.UidUtils;
import com.zcw.cblog.user.dao.UserDao;
import com.zcw.cblog.user.entity.UserEntity;
import com.zcw.cblog.user.vo.UserInfoVo;
import com.zcw.cblog.user.entity.UserRelationEntity;
import com.zcw.cblog.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO:UserService接口实现层
 * @Author YuYu
 * @Date 2020-01-14 15:00
 * @Version 1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    @Override
    public Integer addUser(String username, String password, HttpServletRequest request) {
        //ip
        String ip = request.getRemoteAddr();
        String address = AddressByIpUtils.getAddressByIP(ip);
        UserEntity user = new UserEntity();
        Long uid = UidUtils.getUid();
        //substring()下标含头不含尾,为防止uid没19位从13开始截取
        String endsWith = uid.toString().substring(13);
        user.setUid(uid);
        user.setIp(ip);
        user.setLocation(address);
        user.setNickname("Share_"+endsWith);
        user.setEmail(username);
        user.setPassword(SaltUtils.getPwdPlus(password));
        user.setCreateTime(new Date());
        return userDao.addUser(user);
    }
    /**
     * 查询数据封装到dto类 防止数据暴露
     * @param uid
     * @return
     */
    @Override
    public UserTo findUserById(Long uid) {
        UserEntity user = userDao.findUserById(uid);
        UserTo userVo = new UserTo(
                user.getUid(),
                user.getNickname(),
                user.getAvatar(),
                user.getSex(),
                user.getCreateTime(),
                AccountStatusEnum.NORMAL,
                user.getLastTime(),
                user.getLocation(),
                user.getBirthday(),
                user.getMobile(),
                user.getEmail(),
                user.getSignature(),
                user.getAuthority(),
                user.getExp()
                );
        return userVo;
    }
    /**
     * 修改密码
     * @param username
     * @param password
     * @return
     */
    @Override
    public Integer modifyUserPassword(String username,String password){
        UserEntity user = new UserEntity();
        user.setEmail(username);
        user.setPassword(SaltUtils.getPwdPlus(password));
        //用户更新信息时间
        user.setUpdateTime(new Date());
        return userDao.modifyUserPassword(user);
    }
    /**
     * @param username -- 用户邮箱登录的
     * @return
     */
    @Override
    public UserEntity findUserByName(String username) {
        return userDao.findUserByName(username);
    }

    @Override
    public UserEntityTo findUserToByName(String username) {
        return userDao.findUserToByName(username);
    }
    /**
     * @param userInfoDto  --更新用户信息
     * @param uid
     * @return
     */
    @Override
    public Integer modifyUserInfo(UserInfoVo userInfoDto, String uid) {
        UserEntity user = new UserEntity();
        user.setUid(Long.parseLong(uid));
        user.setNickname(userInfoDto.getNickname());
        user.setLocation(userInfoDto.getLocation());
        user.setSex(Integer.valueOf(userInfoDto.getSex()));
        user.setMobile(userInfoDto.getMobile());
        //用户更新信息时间
        user.setUpdateTime(new Date());
        try {
            user.setBirthday(DateUtils.string2Date(userInfoDto.getBirthday(),"yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
           return 0;
        }
        user.setSignature(userInfoDto.getSignature());
        return userDao.modifyUserInfo(user);
    }

    /**
     * 主要用于修改头像
     * @param
     * @return
     */
    @Override
    public Integer modifyUserInfo(UserTo userVo){
        UserEntity user = new UserEntity();
        user.setUid(userVo.getUid());
        user.setAvatar(userVo.getAvatar());
        return userDao.modifyUserInfo(user);
    }
    /**
     * 更新最后登录时间
     * @param user
     * @return
     */
    @Override
    public Integer updateLastTime(UserEntity user) {
        user.setLastTime(new Date());
        return userDao.updateLastTime(user);
    }

    @Override
    public Integer addRelation(UserRelationEntity userRelation) {
        userRelation.setCreateTime(new Date());
        return userDao.addRelation(userRelation);
    }
    @Override
    public Integer modifyRelation(UserRelationEntity userRelation) {
        userRelation.setModifyTime(new Date());
        return userDao.modifyRelation(userRelation);
    }

    @Override
    public UserRelationEntity findRelation(UserRelationEntity userRelation) {
        return userDao.findRelation(userRelation);
    }

    @Override
    public List<UserTo> findRelationList(Long uid, Integer status, Integer start) {
        return userDao.findRelationList(uid,status,start);
    }

    @Override
    public Integer findRelationTotal(Long uid, Integer status) {
        return userDao.findRelationTotal(uid,status);
    }

    @Override
    public Integer addExpByUid(Long uid, Integer exp) {
        return userDao.addExpByUid(uid,exp);
    }

    @Override
    public List<UserTo> searchUserByKeyword(String keyword) {
        return userDao.searchUserByKeyword(keyword);
    }

    @Override
    public Integer searchUserByKeywordNum(String keyword) {
        Integer  userNum = userDao.searchUserByKeywordNum(keyword);
        return userNum;
    }
}
