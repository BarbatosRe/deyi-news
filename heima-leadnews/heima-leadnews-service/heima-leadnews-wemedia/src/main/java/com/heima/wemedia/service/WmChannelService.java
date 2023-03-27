package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdChannel;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface WmChannelService extends IService<WmChannel> {
    public ResponseResult findAll();

    public ResponseResult delChannel(int id);

    public ResponseResult pageList(ChannelDto dto);

    public ResponseResult saveChannel(AdChannel dto);

    public ResponseResult updateChannel(AdChannel dto);

    public String findOne(Integer id);
}