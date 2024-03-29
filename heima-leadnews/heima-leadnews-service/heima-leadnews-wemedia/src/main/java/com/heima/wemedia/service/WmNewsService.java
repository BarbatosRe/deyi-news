package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import org.springframework.web.bind.annotation.RequestBody;

public interface WmNewsService extends IService<WmNews> {
    public ResponseResult findAll(WmNewsPageReqDto dto);

    public String findOne(WmNewsPageReqDto dto);

    public ResponseResult submitNews(WmNewsDto dto);
    /**
     * 文章的上下架
     * @param dto
     * @return
     */
    public ResponseResult upOrdown(WmNewsDto dto);

    ResponseResult upDate(Integer id);
}
