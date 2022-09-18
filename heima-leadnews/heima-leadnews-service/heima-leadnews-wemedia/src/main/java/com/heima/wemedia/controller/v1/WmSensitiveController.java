package com.heima.wemedia.controller.v1;

import com.heima.model.admin.dtos.AdSensitive;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensitive")
public class WmSensitiveController {

    @Autowired
    private WmSensitiveService WmSensitiveService;

    @DeleteMapping("/del/{id}")
    public ResponseResult delSensitive(@PathVariable(value = "id") int id) {
        return WmSensitiveService.delSensitive(id);
    }

    @PostMapping("/list")
    public ResponseResult findList(@RequestBody SensitiveDto dto) {
        return WmSensitiveService.findList(dto);
    }

    @PostMapping("/save")
    public ResponseResult saveSensitive(@RequestBody AdSensitive adSensitive) {
        return WmSensitiveService.saveSensitive(adSensitive);
    }

    @PostMapping("/update")
    public ResponseResult updateSensitive(@RequestBody AdSensitive adSensitive) {
        return WmSensitiveService.updateSensitive(adSensitive);
    }
}

