package com.heima.user.controllerv1;

import com.heima.model.admin.dtos.AuthDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.user.service.ApUserScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ApUserScanController {
    @Autowired
    private ApUserScanService apUserScanService;

    @PostMapping("/list")
    public ResponseResult findList(@RequestBody AuthDto dto){
      return apUserScanService.findList(dto);
    }

    @PostMapping("/authPass")
    public ResponseResult authPass(@RequestBody AuthDto dto){
        return apUserScanService.authPass(dto);
    }

    @PostMapping("/authFail")
    public ResponseResult authFail(@RequestBody AuthDto dto){
        return apUserScanService.authFail(dto);
    }
}
