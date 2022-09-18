package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface ApArticleSearchService {
    /**
     ES文章分页搜索
     @return
     */
     ResponseResult search(UserSearchDto dto) throws IOException;

}
