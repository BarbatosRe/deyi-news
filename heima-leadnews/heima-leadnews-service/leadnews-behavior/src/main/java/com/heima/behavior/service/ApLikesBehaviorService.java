package com.heima.behavior.service;

import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApLikesBehaviorService {

    /**
     * 点赞行为
     * @param dto
     * @return
     */
    public ResponseResult likes(LikesBehaviorDto dto);

}
