package com.zcw.cblog.article.feign;

import com.zcw.cblog.common.vo.FileVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Chrisz
 * @date 2021/3/18 - 15:53
 */

@FeignClient("cblog-file")
public interface FileFeignService {

    /**
     * 返回文件展示
     * @param aid
     * @return
     */
    @RequestMapping(value = {"/getFilesByAid"})
    List<FileVo> getFilesByAid(@RequestParam("aid") Integer aid);
}
