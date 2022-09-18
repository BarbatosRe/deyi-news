package com.heima.admin.controller.v1;

import com.heima.admin.service.AdSensitiveService;
import com.heima.model.admin.dtos.AdSensitive;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/1api/v1/sensitive")
public class AdSensitiveController {

    @Autowired
    private AdSensitiveService adSensitiveService;

    @DeleteMapping("/del/{id}")
    public ResponseResult delSensitive(@PathVariable(value = "id") int id){
        return adSensitiveService.delSensitive(id);
    };

    @PostMapping("/list")
    public ResponseResult findList(@RequestBody SensitiveDto dto){
        return adSensitiveService.findList(dto);
    }

    @PostMapping("/save")
    public ResponseResult saveSensitive(@RequestBody AdSensitive adSensitive){
        return adSensitiveService.saveSensitive(adSensitive);
    }

    @PostMapping("/update")
    public ResponseResult updateSensitive(@RequestBody AdSensitive adSensitive){
        return adSensitiveService.updateSensitive(adSensitive);
    }



}
