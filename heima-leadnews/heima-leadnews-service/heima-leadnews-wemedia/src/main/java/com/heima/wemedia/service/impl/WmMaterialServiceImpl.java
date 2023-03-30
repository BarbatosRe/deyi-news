package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;



    /**
     * 图片上传
     *
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1.检查参数
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.上传图片到minio中
        //文件名
        String fileName = UUID.randomUUID().toString().replace("_", "");
        //文件名后缀
        String postfix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));

        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("newsImages", fileName + postfix, multipartFile.getInputStream());
            log.info("上传图片到Minio中，fileId：{}", fileId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImpl-上传文件失败");
        }
        //3.保存到数据库中
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileId);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setType((short) 0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);
        //4.返回结果

        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        //1.校验参数
        dto.checkParam();

        //2.分页查询
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2.1是否收藏
        if (dto.getIsCollection() != null && dto.getIsCollection() == 1) {
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection, dto.getIsCollection());
        }

        //2.2按照用户查询
        lambdaQueryWrapper.eq(WmMaterial::getUserId, WmThreadLocalUtil.getUser().getId());

        //2.3按照时间倒序
        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);

        page = page(page, lambdaQueryWrapper);
        //3.返回结果
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * 图片素材的删除
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delImage(Integer id) {
        //参数检查
        if (id==null||id==0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //根据id查询图片数据
        WmMaterial wmMaterial = getById(id);
        if (wmMaterial==null||"".equals(wmMaterial)){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //获取图片url地址
        String imageUrl = wmMaterial.getUrl();
        //在minio中移除图片
        try {
            fileStorageService.delete(imageUrl);
            log.info("删除图片中，fileId：{}", imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除图片异常，fileId：{}",imageUrl );
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"文件删除失败");
        }
        //删除数据库中的image信息
        boolean isdel = removeById(id);
        if (!isdel){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"文件删除失败");
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 图片收藏
     * @param id
     * @return
     */
    @Override
    public ResponseResult collectImage(Integer id) {
        //检查参数
        if (id==null||id==0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //获取当前用户
        WmUser wmUser = WmThreadLocalUtil.getUser();
        //根据id查询到图片素材
        WmMaterial material = getById(id);
        if (material==null||"".equals(material)){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //更改图片收藏的状态
        update(Wrappers.<WmMaterial>lambdaUpdate()
                .set(WmMaterial::getIsCollection,1)
                .eq(WmMaterial::getUserId,wmUser.getId())
                .eq(WmMaterial::getId,id)
        );
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 取消收藏图片
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult cancelCollectimage(Integer id) {
        //检查参数
        if (id==null||id==0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //获取当前用户
        WmUser wmUser = WmThreadLocalUtil.getUser();

        //根据id查询到图片素材
        WmMaterial material = getById(id);
        if (material==null||"".equals(material)){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //更改图片收藏的状态
        if (material.getIsCollection()==0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"没有收藏不能取消收藏");
        }
        update(Wrappers.<WmMaterial>lambdaUpdate()
                .set(WmMaterial::getIsCollection,0)
                .eq(WmMaterial::getUserId,wmUser.getId())
                .eq(WmMaterial::getId,id)
        );
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

}