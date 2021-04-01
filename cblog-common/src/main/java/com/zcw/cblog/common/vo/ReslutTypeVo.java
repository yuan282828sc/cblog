package com.zcw.cblog.common.vo;


import com.zcw.cblog.common.statusenum.*;

/**
 * @Description TODO:返回前端 响应结果
 */
public class ReslutTypeVo {
    //返回信息
    private String message;
    //返回信息的编号
    private int status;
    /**
     * 返回游客  响应信息相关
     * @param visitorStatusEnum
     */
    public ReslutTypeVo(VisitorStatusEnum visitorStatusEnum) {
        this.message = visitorStatusEnum.getMessage();
        this.status = visitorStatusEnum.getStatus();
    }
    /**
     * 管理员  响应信息相关
     * @param adminStatusEnum
     */
    public ReslutTypeVo(AdminStatusEnum adminStatusEnum) {
        this.message = adminStatusEnum.getMessage();
        this.status = adminStatusEnum.getStatus();
    }
    /**
     * 账号状态相关
     * @param accountStatusEnum
     */
    public ReslutTypeVo(AccountStatusEnum accountStatusEnum){
        this.message = accountStatusEnum.getInfo();
        this.status = accountStatusEnum.getStatus();
    }
    /**
     * 博客状态相关
     * @param blogStatusEnum
     */
    public ReslutTypeVo(BlogStatusEnum blogStatusEnum){
        this.message = blogStatusEnum.getMessage();
        this.status = blogStatusEnum.getStatus();
    }
    /**
     * 用户状态相关
     * @param userStatusEnum
     */
    public ReslutTypeVo(UserStatusEnum userStatusEnum){
        this.message = userStatusEnum.getMessage();
        this.status = userStatusEnum.getStatus();
    }
    public ReslutTypeVo() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReslutTypeDto{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
