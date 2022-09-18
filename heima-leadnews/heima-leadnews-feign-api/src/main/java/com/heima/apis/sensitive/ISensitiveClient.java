package com.heima.apis.sensitive;

import com.heima.model.admin.dtos.AdSensitive;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "leadnews-admin")
public interface ISensitiveClient {

    @DeleteMapping("/api/v1/sensitive/del/{id}")
    public ResponseResult delSensitive(@PathVariable(value = "id") int id);

    @PostMapping("/api/v1/sensitive/list")
    public ResponseResult findList(@RequestBody SensitiveDto dto);

    @PostMapping("/api/v1/sensitive/save")
    public ResponseResult saveSensitive(@RequestBody AdSensitive adSensitive);

    @PostMapping("/api/v1/sensitive/update")
    public ResponseResult updateSensitive(@RequestBody AdSensitive adSensitive);

}
