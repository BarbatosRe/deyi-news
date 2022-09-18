package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.admin.dtos.AdSensitive;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.utils.common.DateUtils;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@Transactional
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    @Override
    public ResponseResult delSensitive(int id) {
        //检查参数
        if (id<0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //查询是否有数据
        WmSensitive wmSensitive = wmSensitiveMapper.selectById(id);
        if (wmSensitive ==null|| StringUtils.isBlank(wmSensitive.getSensitives())){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        //删除数据
        wmSensitiveMapper.deleteById(id);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult findList(SensitiveDto dto) {
        //检查参数
        if (dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //检查分页参数
        dto.checkParam();

        //分页查询
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmSensitive> queryWrapper = new LambdaQueryWrapper<>();
        //根据关键词模糊查询
        queryWrapper.like(WmSensitive::getSensitives,dto.getName());

        //按照创建时间倒序查询
        queryWrapper.orderByDesc(WmSensitive::getCreatedTime);

        page = page(page, queryWrapper);
        System.out.println(page);

        PageResponseResult result = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        System.out.println(result.getData());

        return result;
    }

    @Override
    public ResponseResult saveSensitive(AdSensitive adSensitive) {
        //检查参数
        if (adSensitive == null || StringUtils.isBlank(adSensitive.getSensitives())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmSensitive wmSensitive = new WmSensitive();
        //新增数据
        //wmSensitive.setId(adSensitive.getId());
        wmSensitive.setSensitives(adSensitive.getSensitives());
        wmSensitive.setCreatedTime(new Date());
        wmSensitiveMapper.insert(wmSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult updateSensitive(AdSensitive adSensitive) {
        //检查参数
        if (adSensitive == null||StringUtils.isBlank(adSensitive.getSensitives())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //查询数据存不存在
        WmSensitive wmSensitive = wmSensitiveMapper.selectById(adSensitive.getId());
        if (wmSensitive == null){
           return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //执行更新数据
        //wmSensitive.setId(adSensitive.getId());
        wmSensitive.setSensitives(adSensitive.getSensitives());
        wmSensitive.setCreatedTime(new Date());
        wmSensitiveMapper.updateById(wmSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
