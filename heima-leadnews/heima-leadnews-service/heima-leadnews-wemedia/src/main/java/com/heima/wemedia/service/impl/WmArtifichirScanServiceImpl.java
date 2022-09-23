package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.apis.article.IArticleClient;
import com.heima.model.admin.dtos.NewsAuthDto;
import com.heima.model.article.dots.ArticleDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmAuthScanVO;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.common.SaveAppArticle;
import com.heima.wemedia.mapper.WmAuthScanMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmArtifichirScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class WmArtifichirScanServiceImpl implements WmArtifichirScanService {

    @Autowired
    private WmAuthScanMapper authScanMapper;

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Override
    public ResponseResult findlist(NewsAuthDto dto) {
        //检查参数
        if (dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //检查分页参数
        dto.checkParam();
        //执行查询
        IPage<WmAuthScanVO> page=new Page<>(dto.getPage(), dto.getSize());
        page = authScanMapper.findlist(page, dto);

        //封装查询结果
        PageResponseResult result=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        System.out.println(result.getData());
        return result;
    }

    @Override
    public ResponseResult textdetail(int id) {
        //检查参数
        if (id<0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //查询数据
        WmAuthScanVO wmAuthScanVO = authScanMapper.textDetail(id);
        //封装数据
        return ResponseResult.okResult(wmAuthScanVO);
    }

    @Override
    public ResponseResult authFail(NewsAuthDto dto) {
        //检查参数
        if (dto == null || dto.getId()<0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //根据id查询数据
        WmNews wmNews = wmNewsMapper.selectById(dto.getId());
        if (wmNews == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //修改文章状态为不通过，并且添加拒绝信息
        wmNews.setStatus((short) 2);
        wmNews.setReason(dto.getMsg());
        wmNewsMapper.updateById(wmNews);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private SaveAppArticle saveAppArticle;

    @Override
    @Async
    public ResponseResult authPass(NewsAuthDto dto) {
        //检查参数
        if (dto == null || dto.getId()<0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //根据id查询数据
        WmNews wmNews = wmNewsMapper.selectById(dto.getId());
        if (wmNews == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //修改文章状态为通过
        wmNews.setStatus((short) 8);

        wmNewsMapper.updateById(wmNews);
        //人工审核通过，保存同步到app端
        ResponseResult responseResult = saveAppArticle.saveAppArticle(wmNews);
        if (!responseResult.getCode().equals(200)){
            throw new RuntimeException("WmNewsAutoScanServiceImpl-文章审核，保存app端相关文章数据失败");
        }
        //回填id
        wmNews.setArticleId((Long) responseResult.getData());
        wmNews.setStatus((short) 9);
        wmNews.setReason("审核成功");
        wmNewsMapper.updateById(wmNews);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
