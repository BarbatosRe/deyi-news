package com.heima.admin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdSensitiveMapper;
import com.heima.admin.service.AdSensitiveService;
import com.heima.apis.sensitive.ISensitiveClient;
import com.heima.model.admin.dtos.AdSensitive;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.utils.common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.util.Date;

@Service
@Slf4j
@Transactional
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper, WmSensitive>implements AdSensitiveService {

    @Autowired
    private ISensitiveClient iSensitiveClient;

    @Override
    public ResponseResult delSensitive(int id) {
        return iSensitiveClient.delSensitive(id);
    }

    @Override
    public ResponseResult findList(SensitiveDto dto) {
        return iSensitiveClient.findList(dto);
    }

    @Override
    public ResponseResult saveSensitive(AdSensitive adSensitive) {
        return iSensitiveClient.saveSensitive(adSensitive);
    }

    @Override
    public ResponseResult updateSensitive(AdSensitive adSensitive) {
        return iSensitiveClient.updateSensitive(adSensitive);
    }
}
