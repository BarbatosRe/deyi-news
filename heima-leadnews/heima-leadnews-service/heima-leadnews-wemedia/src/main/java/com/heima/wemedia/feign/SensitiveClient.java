package com.heima.wemedia.feign;

import com.heima.apis.sensitive.ISensitiveClient;
import com.heima.model.admin.dtos.AdSensitive;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


public class SensitiveClient implements ISensitiveClient {

    @Autowired
    private WmSensitiveService wmSensitiveService;

    @Override
    @DeleteMapping("/api/v1/sensitive/del/{id}")
    public ResponseResult delSensitive(@PathVariable(value = "id") int id){
        return wmSensitiveService.delSensitive(id);
    };
    @Override
    @PostMapping("/api/v1/sensitive/list")
    public ResponseResult findList(@RequestBody SensitiveDto dto){
        return wmSensitiveService.findList(dto);
    }

    @Override
    @PostMapping("/api/v1/sensitive/save")
    public ResponseResult saveSensitive(@RequestBody AdSensitive adSensitive){
        return wmSensitiveService.saveSensitive(adSensitive);
    }

    @Override
    @PostMapping("/api/v1/sensitive/update")
    public ResponseResult updateSensitive(@RequestBody AdSensitive adSensitive){
        return wmSensitiveService.updateSensitive(adSensitive);
    }
}
