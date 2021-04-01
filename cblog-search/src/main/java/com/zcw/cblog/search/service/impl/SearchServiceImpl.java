package com.zcw.cblog.search.service.impl;

import com.zcw.cblog.common.to.ArticleTo;
import com.zcw.cblog.common.to.UserTo;
import com.zcw.cblog.search.feign.ArticleFeignService;
import com.zcw.cblog.search.feign.UserFeignService;
import com.zcw.cblog.search.service.SearchService;
import com.zcw.cblog.search.vo.SearchParam;
import com.zcw.cblog.search.vo.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chrisz
 * @date 2021/3/16 - 8:34
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    ArticleFeignService articleFeignService;
    @Autowired
    UserFeignService userFeignService;
    /**
     *
     * @param param 检索所有的条件
     * @return
     */
    @Override
    public SearchResponse search(SearchParam param) {

        String keyword = param.getKeyword();
        SearchResponse response = new SearchResponse();
        List<ArticleTo> articleTos = articleFeignService.searchBlogListByKeyword(keyword);
        Integer articleNum = articleFeignService.searchBlogListByKeywordNum(keyword);
        List<UserTo> userTos = userFeignService.searchUserByKeyword(keyword);
        Integer userNum = userFeignService.searchUserByKeywordNum(keyword);
        response.setArticles(articleTos);
        response.setUserNum(userNum);
        response.setArticleNum(articleNum);
        response.setUsers(userTos);

        return response;
    }
}
