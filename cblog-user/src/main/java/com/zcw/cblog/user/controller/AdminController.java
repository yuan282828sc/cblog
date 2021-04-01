package com.zcw.cblog.user.controller;


import com.zcw.cblog.common.statusenum.AdminStatusEnum;
import com.zcw.cblog.common.vo.ReslutTypeVo;
import com.zcw.cblog.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description TODO:管理员权限
 */

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 文章收藏  置顶
     *
     * @param top
     * @param aid
     * @return
     */
    @PostMapping("/setTop")
    @ResponseBody
    public ReslutTypeVo topBlog(@RequestParam("top") String top,
                                @RequestParam("aid") String aid) {
        Integer type = Integer.valueOf(top);
        Integer id = Integer.valueOf(aid);
        try {
            Integer integer = adminService.setTopBlog(type,id);
            if (integer == 1) {
//                设置成功
                return new ReslutTypeVo(AdminStatusEnum.Blog_Top_Modify_OK);
            } else {
                return new ReslutTypeVo(AdminStatusEnum.Blog_Modify_Exception);
            }
        } catch (Exception e) {
            return new ReslutTypeVo(AdminStatusEnum.Blog_Modify_Exception);
        }
    }

    /**
     * 文章收藏
     *
     * @param boutique 精贴
     * @param aid
     * @return
     */
    @PostMapping("/setBoutique")
    @ResponseBody
    public ReslutTypeVo boutiqueBlog(@RequestParam("boutique") String boutique,
                                      @RequestParam("aid") String aid) {
        Integer type = Integer.valueOf(boutique);
        Integer id = Integer.valueOf(aid);

        try {
            Integer integer = adminService.setBoutiqueBlog(type, id);
            if (integer == 1) {
//                设置成功
                return new ReslutTypeVo(AdminStatusEnum.Blog_Boutique_Modify_OK);
            } else {
                return new ReslutTypeVo(AdminStatusEnum.Blog_Modify_Exception);
            }
        } catch (Exception e) {
            return new ReslutTypeVo(AdminStatusEnum.Blog_Modify_Exception);
        }
    }
}
