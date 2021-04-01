package com.zcw.cblog.message.service.impl;

import com.zcw.cblog.common.to.MessageTo;
import com.zcw.cblog.common.vo.ReplyVo;
import com.zcw.cblog.message.dao.MessageDao;
import com.zcw.cblog.message.entity.MessageEntity;
import com.zcw.cblog.message.service.MessageService;
import com.zcw.cblog.message.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO:
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public Integer addMessage(ReplyVo replyDto) {
        MessageEntity message = new MessageEntity();
        //评论接受者id
        Long rid = replyDto.getRid();
        Integer aid = replyDto.getAid();
        if (rid == null) {
            //评论文章--发送给博主
            message.setStatus(0);
            message.setRid(replyDto.getAuid());
        } else {
            //回复评论--发送给评论者
            message.setRid(rid);
            message.setStatus(1);
        }
        message.setUid(replyDto.getUid());
        message.setAid(aid);
        message.setContent(replyDto.getContent());
        message.setCreateTime(new Date());
        return messageDao.addMessage(message);
    }



    @Override
    public List<MessageVo> findMessageByRid(Long uid, Integer start) {
        return messageDao.findMessageByRid(uid,start);
    }

    @Override
    public List<MessageVo> findMessageByUid(Long uid) {
        return messageDao.findMessageByUid(uid);
    }

    @Override
    public Integer findTotalByRid(Long uid) {
        return messageDao.findTotalByRid(uid);
    }

    @Override
    public Integer setReadStatus(MessageEntity message) {
        return messageDao.setReadStatus(message);
    }

    @Override
    public Integer sendMessage(MessageEntity message) {
        message.setCreateTime(new Date());
        //发送私信，aid默认1
        message.setAid(1);
        return messageDao.addMessage(message);
    }

    @Override
    public Integer findNewMessageTotal(Long rid) {
        return messageDao.findNewMessageTotal(rid);
    }
}
