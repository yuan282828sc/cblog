package com.zcw.cblog.message.controller;

import com.zcw.cblog.common.vo.ReplyVo;
import com.zcw.cblog.message.entity.MessageEntity;
import com.zcw.cblog.message.service.MessageService;
import com.zcw.cblog.message.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chrisz
 * @date 2021/2/2 - 17:12
 */

@RestController
//@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * 查看个人消息
     */
    @GetMapping("/findMessageByRid")
    public List<MessageVo> findMessageByRid(@RequestParam("uid") Long uid, @RequestParam("start") Integer start){

        List<MessageVo> messageList = messageService.findMessageByRid(uid, start);
        return messageList;
    }

    /**
     * 查看消息总数
     */
    @GetMapping("/findTotalByRid")
    public Integer findTotalByRid(@RequestParam("uid") Long uid){

        Integer total = messageService.findTotalByRid(uid);
        return total;
    }

    /**
     * 设置已读
     */
    @PostMapping("/setReadStatus")
    public Integer setReadStatus(@RequestBody MessageEntity messageEntity){
        Integer total = messageService.setReadStatus(messageEntity);
        return total;
    }


    /**
     * 发消息
     */
    @PostMapping("/sendMessage")
    public Integer sendMessage(@RequestBody MessageEntity messageEntity){
        Integer total = messageService.sendMessage(messageEntity);
        return total;
    }

    /**
     * 查询消息记录
     */
    @GetMapping("/findMessageByUid")
    public List<MessageVo> findMessageByUid(@RequestParam("uid") Long uid){

        List<MessageVo> messageList = messageService.findMessageByUid(uid);
        return messageList;
    }

    /**
     * 评论发消息
     */
    @PostMapping("/addMessage")
    public Integer addMessage(@RequestBody ReplyVo replyVo){
        Integer total = messageService.addMessage(replyVo);
        return total;
    }

    /**
     * 新消息总数
     */
    @GetMapping("/findNewMessageTotal")
    public Integer findNewMessageTotal(@RequestParam("rid")Long rid){

        Integer newMessageTotal = messageService.findNewMessageTotal(rid);
        return newMessageTotal;
    }

}
