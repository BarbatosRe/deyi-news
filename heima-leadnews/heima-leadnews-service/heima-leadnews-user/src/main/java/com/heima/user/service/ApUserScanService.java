package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AuthDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUserRealname;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApUserScanService extends IService<ApUserRealname> {

    /**
     * 查找全部的用户信息
     * @param dto
     * @return
     */
    public ResponseResult findList(AuthDto dto);

    /**
     * 通过审核
     * @param dto
     * @return
     */
    public ResponseResult authPass(@RequestBody AuthDto dto);

    /**
     * 审核不通过
     * @param dto
     * @return
     */
    public ResponseResult authFail(@RequestBody AuthDto dto);

}
