package com.heima.wemedia.controller.v1;

import com.heima.model.admin.dtos.NewsAuthDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmArtifichirScanService;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class WmArtifichirScanController {

    @Autowired
    private WmArtifichirScanService wmArtifichirScanService;


    @PostMapping("/list_vo")
    public ResponseResult findlist(@RequestBody NewsAuthDto dto){
        return wmArtifichirScanService.findlist(dto);
    }

    @GetMapping("/one_vo/{id}")
    public ResponseResult textdetail(@PathVariable(value = "id") int id){
        return wmArtifichirScanService.textdetail(id);
    }

    @PostMapping("/auth_fail")
    public ResponseResult authFail(@RequestBody NewsAuthDto dto){
        return wmArtifichirScanService.authFail(dto);
    }

    @PostMapping("/auth_pass")
    public ResponseResult authPass(@RequestBody NewsAuthDto dto){
        return wmArtifichirScanService.authPass(dto);
    }
}
