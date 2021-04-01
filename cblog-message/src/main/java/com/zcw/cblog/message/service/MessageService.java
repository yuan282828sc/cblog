package com.zcw.cblog.message.service;

import com.zcw.cblog.common.to.MessageTo;
import com.zcw.cblog.common.vo.ReplyVo;
import com.zcw.cblog.message.entity.MessageEntity;
import com.zcw.cblog.message.vo.MessageVo;


import java.util.List;

/**
 * @Description TODO:消息相关
 */
public interface MessageService {
    /**
     * 添加消息记录
     * @param replyDto
     * @return
     */
    Integer addMessage(ReplyVo replyDto);
    /**
     * 查询记录消息 --接受方
     * @param uid
     * @param start
     * @return
     */
    List<MessageVo> findMessageByRid(Long uid, Integer start);
    /**
     * 查询记录消息 --发送方
     * @param uid
     * @return
     */
    List<MessageVo> findMessageByUid(Long uid);
    /**
     * 接受方 查看消息总数
     * @param uid
     * @return
     */
    Integer findTotalByRid(Long uid);
    /**
     * 设置为已读
     * @param message
     * @return
     */
    Integer setReadStatus(MessageEntity message);
    /**
     * 私信添加   消息记录
     * @param message
     * @return
     */
    Integer sendMessage(MessageEntity message);
    /**
     * 新消息记录数
     * @param rid
     * @return
     */
    Integer findNewMessageTotal(Long rid);
}
