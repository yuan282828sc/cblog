package com.zcw.cblog.search.feign;

import com.zcw.cblog.common.to.ArticleTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Chrisz
 * @date 2021/3/16 - 10:58
 */
@FeignClient("cblog-article")
public interface ArticleFeignService {

    /**
     * 检索服务-搜索博客文章
     */
    @GetMapping("/article/searchBlogListByKeyword")
    List<ArticleTo> searchBlogListByKeyword(@RequestParam("keyword") String keyword);

    @GetMapping("/article/searchBlogListByKeywordNum")
    Integer searchBlogListByKeywordNum(@RequestParam("keyword") String keyword);

}
