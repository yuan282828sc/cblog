package com.zcw.cblog.message.config.shiro;

import com.zcw.cblog.common.constant.Salt;
import com.zcw.cblog.common.to.UserEntityTo;
import com.zcw.cblog.message.feign.UserFeignService;
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
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserFeignService userFeignService;


    /**
     * 角色授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取当前登录的对象--- 数据库授权
        Subject subject = SecurityUtils.getSubject();
        UserEntityTo currentUser = (UserEntityTo) subject.getPrincipal();
        Integer authority = currentUser.getAuthority();
//        根据数字判断是否有用户权限
        if (authority != null && authority > 0) {
            info.addRole("user");
        }
        return info;
    }

    /**
     * 登录验证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //根据Token中的username
        //从数据库获取对应用户名密码的用户
        UserEntityTo user = userFeignService.findUserByName(token.getUsername());
/*        //为了安全起见，自己封装一个dto类，防止重要信息泄露
        UserDto userDto = userServiceImpl.findUserById(user.getUid());
        //记录新消息数
        Long rid = userDto.getUid();
        Integer newMessageTotal = messageServiceImpl.findNewMessageTotal(rid);
        //放回前端页面
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        if (newMessageTotal > 0) {
            session.setAttribute("newMessageTotal", newMessageTotal);
        }
        session.setAttribute("loginUser", userDto);*/
        /*加盐  salt*/
        ByteSource byteSource = ByteSource.Util.bytes(Salt.ADD_SALT);
        //密码认证
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                byteSource,
                getName());// 指定当前 Realm 的类名);
        return info;
    }
}
