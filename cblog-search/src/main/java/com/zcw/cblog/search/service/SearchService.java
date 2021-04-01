package com.zcw.cblog.search.service;

import com.zcw.cblog.search.vo.SearchParam;
import com.zcw.cblog.search.vo.SearchResponse;

/**
 * @author Chrisz
 * @date 2021/3/16 - 8:34
 */
public interface SearchService {

    /**
     * 检索
     * @param param 检索所有的条件
     * @return 返回检索的商品得结果
     */
    SearchResponse search(SearchParam param);
}
