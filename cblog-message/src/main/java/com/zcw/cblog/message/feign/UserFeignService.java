package com.zcw.cblog.message.feign;

import com.zcw.cblog.common.to.UserEntityTo;
import com.zcw.cblog.common.to.UserTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("cblog-user")
public interface UserFeignService {

    /**
     * 修改用户头像
     *
     * @param userTo
     * @return
     */
    @PostMapping("/setavatar")
    @ResponseBody
    Integer modifyAvatar(@RequestBody UserTo userTo);

    /**
     * 查询用户--邮箱登录的
     *
     * @param
     * @param
     * @return
     */
    @GetMapping("/findUserByName")
    @ResponseBody
    UserEntityTo findUserByName(@RequestParam("username") String username);

    /**
     * 查询用户信息
     *
     * @param
     * @param
     * @return
     */
    @GetMapping("/findUserById")
    UserTo findUserById(@RequestParam("uid")Long uid);

    /**
     * 更新最后登录时间
     * @param
     * @return
     */
    @PostMapping("/updateLastTime")
    @ResponseBody
    Integer updateLastTime(@RequestBody UserEntityTo userEntity);
}
