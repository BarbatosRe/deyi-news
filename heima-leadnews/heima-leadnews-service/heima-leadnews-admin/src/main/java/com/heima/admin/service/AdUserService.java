package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmLoginDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdUserService extends IService<AdUser> {
    /**
     * 管理平台登录
     * @param dto
     * @return
     */
    public ResponseResult login(AdUserDto dto);
}
