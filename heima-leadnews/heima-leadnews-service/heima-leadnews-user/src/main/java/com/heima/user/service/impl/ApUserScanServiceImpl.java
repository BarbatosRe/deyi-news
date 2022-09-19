package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.admin.dtos.AuthDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.user.mapper.ApUserScanMapper;
import com.heima.user.service.ApUserScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@Transactional
public class ApUserScanServiceImpl extends ServiceImpl<ApUserScanMapper, ApUserRealname> implements ApUserScanService {


    @Autowired
    private ApUserScanMapper apUserScanMapper;
    /**
     * 查找全部的用户信息
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(AuthDto dto) {
        //检测参数
        if (dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页查询
        LambdaQueryWrapper<ApUserRealname> queryWrapper =new LambdaQueryWrapper();
        IPage page = new Page(dto.getPage(), dto.getSize());
        //可根据审核状态条件查询
        if (dto.getStatus() == null){
            //List<ApUserRealname> apUserRealnames = apUserScanMapper.selectList(null);
            page = page(page);
        }else {
            queryWrapper.eq(ApUserRealname::getStatus,dto.getStatus());
            page = page(page,queryWrapper);
        }
        PageResponseResult result=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        System.out.println(result.getData());
        return result;
    }

    /**
     * 通过审核
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult authPass(AuthDto dto) {
        //检查参数
        if (dto == null || dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        try {
            //查询数据
            ApUserRealname apUserRealname = apUserScanMapper.selectById(dto.getId());
            if (apUserRealname == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }

            //修改状态变更为通过 9
            apUserRealname.setStatus((short) 9);
            apUserRealname.setUpdatedTime(new Date());
            apUserScanMapper.updateById(apUserRealname);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 审核不通过
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult authFail(AuthDto dto) {
        //检查参数
        if (dto == null || dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        try {
            //查询数据
            ApUserRealname apUserRealname = apUserScanMapper.selectById(dto.getId());
            if (apUserRealname == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }

            //修改状态变更为不通过 2
            apUserRealname.setStatus((short) 2);
            apUserRealname.setUpdatedTime(new Date());
            apUserRealname.setReason(dto.getMsg());
            apUserScanMapper.updateById(apUserRealname);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

}
