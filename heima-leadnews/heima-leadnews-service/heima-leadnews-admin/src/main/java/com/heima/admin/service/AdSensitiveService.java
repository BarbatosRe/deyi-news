package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdSensitive;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmSensitive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdSensitiveService extends IService<WmSensitive> {
    public ResponseResult delSensitive(int id);

    public ResponseResult findList(SensitiveDto dto);

    public ResponseResult saveSensitive(AdSensitive adSensitive);

    public ResponseResult updateSensitive(AdSensitive adSensitive);
}
