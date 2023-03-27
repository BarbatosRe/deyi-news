package com.heima.behavior.service.impl;

import com.heima.behavior.service.ApLikesBehaviorService;
import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {
    /**
     * 点赞行为
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult likes(LikesBehaviorDto dto) {
        //检查参数
        if (dto == null || checkParam(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //点赞，保存数据


        return null;
    }


    public boolean checkParam(LikesBehaviorDto dto){
        if (dto.getOperation() < 0 || dto.getOperation() > 1
                || dto.getType() < 0 || dto.getType() > 2 || dto.getArticleId() == 0){
            return true;
        }
        return false;
    }
}
