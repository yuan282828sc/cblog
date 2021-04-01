package com.zcw.cblog.article.feign;

import com.zcw.cblog.common.vo.ReplyVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient("cblog-message")
public interface MessageFeignService {

    /**
     * 评论发消息
     */
    @PostMapping("/addMessage")
    Integer addMessage(@RequestBody ReplyVo replyVo);


    /**
     * 新消息数
     */
    @GetMapping("/findNewMessageTotal")
    Integer findNewMessageTotal(@RequestParam("rid")Long rid);
}
