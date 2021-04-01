package com.zcw.cblog.message.dao;

import com.zcw.cblog.common.to.MessageTo;
import com.zcw.cblog.message.entity.MessageEntity;
import com.zcw.cblog.message.vo.MessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO:消息
 */
@Mapper
@Repository
public interface MessageDao {

    /**
     * 添加消息记录
     * @param message
     * @return
     */
    Integer addMessage(MessageEntity message);


    /**
     * 查询记录消息 --接受方
     * @param uid
     * @param start
     * @return
     */
    List<MessageVo> findMessageByRid(@Param("uid") Long uid, @Param("start") Integer start);

    /**
     * 查询记录消息 --发送方
     * @param uid
     * @return
     */
    List<MessageVo> findMessageByUid(@Param("uid") Long uid);

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
     * 新消息记录数
     * @param rid
     * @return
     */
    Integer findNewMessageTotal(Long rid);
}
