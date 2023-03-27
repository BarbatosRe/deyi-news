package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.admin.dtos.AdChannel;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {

    @Autowired
    private WmChannelMapper wmChannelMapper;


    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }

    @Override
    public ResponseResult delChannel(int id) {
        //检查参数
        if (id<0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //判断频道是否禁用
        WmChannel wmChannel = wmChannelMapper.selectById(id);
        if (wmChannel.getStatus()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"必须禁用才能继续操作");
        }
        //根据id删除频道
        try {
            wmChannelMapper.deleteById(id);
        } catch (Exception e) {
           e.printStackTrace();
           return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult pageList(ChannelDto dto) {
        //检查参数
        if (dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //检查分页参数
        dto.checkParam();
        //查找数据
        LambdaQueryWrapper<WmChannel> wrapper = new LambdaQueryWrapper<>();
        //按照频道名称模糊查询
        wrapper.like(WmChannel::getName,dto.getName());
        //可以按照状态进行精确查找（1：启用   true           0：禁用   false）
        //查询需要按照创建时间倒序查询
        wrapper.orderByDesc(WmChannel::getCreatedTime);
        IPage page =new Page(dto.getPage(),dto.getSize());
        page = page(page,wrapper);

        ResponseResult responseResult=new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        System.out.println(responseResult.getData());
        return responseResult;
    }

    @Override
    public ResponseResult saveChannel(AdChannel dto) {
        //检查参数
        if (dto == null || StringUtils.isBlank(dto.getName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //添加数据
        try {
            WmChannel wmChannel = new WmChannel();
            BeanUtils.copyProperties(dto,wmChannel);
            wmChannel.setCreatedTime(new Date());
            wmChannelMapper.insert(wmChannel);
        } catch (BeansException e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult updateChannel(AdChannel dto) {
        //检查参数
        if (dto == null || StringUtils.isBlank(dto.getName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //修改数据
        try {
            WmChannel wmChannel = wmChannelMapper.selectById(dto.getId());
            if (wmChannel == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
            BeanUtils.copyProperties(dto,wmChannel);
            wmChannel.setCreatedTime(new Date());
            wmChannelMapper.updateById(wmChannel);
        } catch (BeansException e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public String findOne(Integer id) {
        if (id==null||id==0){
            return null;
        }
        WmChannel wmChannel = getById(id);
        String name = wmChannel.getName();
        return name;
    }
}