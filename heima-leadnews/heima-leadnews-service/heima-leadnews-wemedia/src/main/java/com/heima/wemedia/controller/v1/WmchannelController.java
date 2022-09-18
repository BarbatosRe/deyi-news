package com.heima.wemedia.controller.v1;

import com.heima.model.admin.dtos.AdChannel;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
public class WmchannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult findAll(){
        return wmChannelService.findAll();
    }

    @GetMapping("/del/{id}")
    public ResponseResult delChannel(@PathVariable(value = "id") int id){
        return wmChannelService.delChannel(id);
    }

    @PostMapping("/list")
    public ResponseResult pageList(@RequestBody ChannelDto dto){
        return wmChannelService.pageList(dto);
    }

    @PostMapping("/save")
    public ResponseResult saveChannel(@RequestBody AdChannel dto){
        return wmChannelService.saveChannel(dto);
    }

    @PostMapping("/update")
    public ResponseResult updateChannel(@RequestBody AdChannel dto){
        return wmChannelService.updateChannel(dto);
    }
}
