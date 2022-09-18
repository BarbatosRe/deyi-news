package com.heima.admin.controller.v1;

import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AdUserLoginController {

    @Autowired
    private AdUserService adUserService;
    /**
     * 管理平台登录
     * @param dto
     * @return
     */
    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdUserDto dto){

        return adUserService.login(dto);
    }
}
