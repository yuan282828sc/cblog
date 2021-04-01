package com.zcw.cblog.user.config.shiro;

import com.zcw.cblog.common.constant.Salt;
import com.zcw.cblog.common.to.UserEntityTo;
import com.zcw.cblog.user.entity.UserEntity;
import com.zcw.cblog.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO:自定义Realm  extends AuthorizingRealm
 * 自定义Realm用于查询用户的角色和权限信息并保存到权限管理器
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;


    /**
     * 角色授权
     * 权限配置类
     * 执行授权逻辑
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //获取当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        UserEntityTo currentUser = (UserEntityTo) subject.getPrincipal();
        Integer authority = currentUser.getAuthority();
//添加角色和权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        根据数字判断是否有用户权限
        if (authority != null && authority > 0) {
            info.addRole("user");
        }
        return info;
    }

    /**
     * 登录验证
     * 认证配置类
     * 执行认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //根据Token中的username
        //从数据库获取对应用户名密码的用户

        //1.判断用户名  token中的用户信息是登录时候传进来的
        UserEntityTo user = userService.findUserToByName(token.getUsername());
        /*加盐  salt*/
        ByteSource byteSource = ByteSource.Util.bytes(Salt.ADD_SALT);
        //密码认证
        //这里验证authenticationToken和simpleAuthenticationInfo的信息

        //2.判断密码
        //第二个字段是user.getPassword()，注意这里是指从数据库中获取的password。第三个字段是realm，即当前realm的名称。
        //这块对比逻辑是先对比username，但是username肯定是相等的，所以真正对比的是password。
        //从这里传入的password（这里是从数据库获取的）和token（filter中登录时生成的）中的password做对比，如果相同就允许登录，
        // 不相同就抛出IncorrectCredentialsException异常。
        //如果认证不通过，就不会执行下面的授权方法了
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                byteSource,
                getName());// 指定当前 Realm 的类名);
        return info;
    }
}
