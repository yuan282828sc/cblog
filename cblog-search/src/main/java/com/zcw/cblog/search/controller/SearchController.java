package com.zcw.cblog.search.controller;

import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.search.service.SearchService;
import com.zcw.cblog.search.vo.SearchParam;
import com.zcw.cblog.search.vo.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Chrisz
 * @date 2021/3/16 - 9:22
 */
@Controller
public class SearchController {

    @Autowired
    SearchService searchService;


    /**
     * 检索
     * @param param 检索所有的条件
     * @return      返回检索的商品得结果
     */
    //自动将页面提交过来的所有请求参数封装成指定的对象
    @GetMapping("/search")
    public String searchPage(SearchParam param, Model model, HttpServletRequest request){
//        HttpSession session = request.getSession();
//        UserTo loginUser = (UserTo) session.getAttribute("loginUser");
//        Long uid = loginUser.getUid();
        SearchResponse response = searchService.search(param);

        model.addAttribute("response", response);
        //因为使用了thymeleaf，里面配置好了前缀，后缀
        return "search";
    }
}
