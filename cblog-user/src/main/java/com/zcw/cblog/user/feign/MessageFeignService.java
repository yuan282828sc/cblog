package com.zcw.cblog.user.feign;



import com.zcw.cblog.common.to.MessageTo;
import com.zcw.cblog.user.vo.MessageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chrisz
 * @date 2021/2/2 - 16:59
 */
@FeignClient("cblog-message")
public interface MessageFeignService {

    /**
     * 查看个人消息
     */
    @GetMapping("/findMessageByRid")
    @ResponseBody
    List<MessageVo> findMessageByRid(@RequestParam("uid")Long uid, @RequestParam("start")Integer start);

    /**
     * 查看消息总数
     */
    @GetMapping("/findTotalByRid")
    @ResponseBody
    Integer findTotalByRid(@RequestParam("uid") Long uid);

    /**
     * 设置已读
     */
    @PostMapping("/setReadStatus")
    @ResponseBody
    Integer setReadStatus(@RequestBody MessageTo messageTo);

    /**
     * 发消息
     */
    @PostMapping("/sendMessage")
    @ResponseBody
    Integer sendMessage(@RequestBody MessageTo messageTo);

    /**
     * 查询消息记录
     */
    @GetMapping("/findMessageByUid")
    @ResponseBody
    List<MessageVo> findMessageByUid(@RequestParam("uid") Long uid);

    /**
     * 新消息总数
     */
    @GetMapping("/findNewMessageTotal")
    @ResponseBody
    Integer findNewMessageTotal(@RequestParam("rid")Long rid);
}
