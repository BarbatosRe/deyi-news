package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 图片上传
     * @param multipartFile
     * @return
     */
    public ResponseResult uploadPicture(MultipartFile multipartFile);

    public ResponseResult findList(WmMaterialDto dto);


    /**
     * 图片素材的删除
     * @param id
     * @return
     */
    ResponseResult delImage(Integer id);

    /**
     * 图片收藏
     * @param id
     * @return
     */
    ResponseResult collectImage(Integer id);

    /**
     * 取消收藏图片
     * @param id
     * @return
     */
    ResponseResult cancelCollectimage(Integer id);
}