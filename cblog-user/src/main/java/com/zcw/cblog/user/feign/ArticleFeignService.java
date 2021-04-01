package com.zcw.cblog.user.feign;

import com.zcw.cblog.common.to.ArticleTo;
import com.zcw.cblog.common.to.FavoriteArticleTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chrisz
 * @date 2021/2/1 - 17:39
 */
@FeignClient("cblog-article")
public interface ArticleFeignService {

    /**
     * 最近十条---公开的
     * @return
     */
    @GetMapping("/article/findBlogByUid")
    @ResponseBody
    List<ArticleTo> findBlogByUid(@RequestParam("uid")Long uid,
                                  @RequestParam("open_procedure")Integer open_procedure,
                                  @RequestParam("start")Integer start);


    /**
     * 公开的文章总数
     * @return
     */
    @GetMapping("/article/findTotalByUid")
    @ResponseBody
    Integer findTotalByUid(@RequestParam("uid")Long uid, @RequestParam("open_procedure")Integer open_procedure);

    /**
     * 最近十条----收藏的
     * @return
     */
    @GetMapping("/article/findCollectBlogList")
    List<ArticleTo> findCollectBlogList(@RequestParam("uid")Long uid, @RequestParam("start")Integer start);

    /**
     * 收藏的文章总数
     * @return
     */
    @PostMapping("/article/findCollectBlog")
    Integer findCollectBlog(@RequestBody FavoriteArticleTo favoriteArticleTo);

}
