package com.heima.wemedia.service;

import com.heima.model.admin.dtos.NewsAuthDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface WmArtifichirScanService {

    public ResponseResult findlist(NewsAuthDto dto);

    public ResponseResult textdetail(int id);

    public ResponseResult authFail(NewsAuthDto dto);

    public ResponseResult authPass(@RequestBody NewsAuthDto dto);
}
