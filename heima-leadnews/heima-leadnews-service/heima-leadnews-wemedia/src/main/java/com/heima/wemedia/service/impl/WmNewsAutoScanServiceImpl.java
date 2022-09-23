package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.apis.article.IArticleClient;
import com.heima.common.aliyun.GreenImageScan2;
import com.heima.common.aliyun.GreenTextScan2;
import com.heima.model.article.dots.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.common.SaveAppArticle;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {


    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private SaveAppArticle saveAppArticle;

    /**
     * 自媒体文章审核
     *
     * @param id 自媒体文章id
     */
    @Override
    @Async //标明这是一个异步的方法
    public void autoScanWmNews(Integer id) {
        System.out.println(id);

        //1.查询自媒体文章
        WmNews wmNews = wmNewsMapper.selectById(id);

        if (wmNews == null){
            throw new RuntimeException("WmNewsAutoScanServiceImpl-文章不存在");
        }
        //判断状态是否是已提交
        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            //从内容中提取纯文本内容和图片
            Map<String,Object> textAndImages = handleTextAndImages(wmNews);
            //自管理的敏感词过滤
            boolean isSensitive = handleSensitiveScan(String.valueOf(textAndImages.get("content")), wmNews);
            if(!isSensitive) return;
            //2.审核文本内容  阿里云接口
            boolean isTextScan = handleTextScan(String.valueOf(textAndImages.get("content")),wmNews);
            if (!isTextScan)return;
            //3.审核图片  阿里云接口
            boolean imageScan = handleImageScan((List<String>) textAndImages.get("images"), wmNews);
            if (!imageScan)return;
            //4.审核成功，保存app端的相关的文章数据
            ResponseResult responseResult = saveAppArticle.saveAppArticle(wmNews);
            if(!responseResult.getCode().equals(200)){
                throw new RuntimeException("WmNewsAutoScanServiceImpl-文章审核，保存app端相关文章数据失败");
            }
            //回填article_id
            wmNews.setArticleId((Long) responseResult.getData());
            updateWmNews(wmNews, (short) 9,"审核成功");
        }

    }

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;


    private boolean handleSensitiveScan(String content, WmNews wmNews) {
        boolean flag=true;

        //获取所有敏感词
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        List<String> sensitiveList = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());

        //初始化敏感库
        SensitiveWordUtil.initMap(sensitiveList);

        //查看文章中是否含敏感词
        Map<String, Integer> matchWords = SensitiveWordUtil.matchWords(content);
        if (matchWords.size()>0){
            updateWmNews(wmNews, (short) 2,"当前文本中存在违规内容"+matchWords);
            flag=false;
        }

        return flag;
    }



    @Autowired
    private GreenTextScan2 greenTextScan2;

    @Autowired
    private GreenImageScan2 greenImageScan2;

    /**
     * 审核图片
     * @param images
     * @param wmNews
     * @return
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews){
        boolean flag=true;

        if (images == null|| images.size()==0){
            return flag;
        }

        //图片去重
        images = images.stream().distinct().collect(Collectors.toList());

        //审核图片
        try {
            Map resImage = greenImageScan2.imageScan(images);
            if (resImage !=null){
                if (resImage.get("result").equals("1")){
                    flag=false;
                    updateWmNews(wmNews,(short)2,"当前文章中存在违规内容");
                }

                //不确定信息需要人工审核
                if (resImage.get("result").equals("2")){
                    flag=false;
                    updateWmNews(wmNews, (short) 3,"当前文章中存在不确定内容");
                }
            }
        } catch (Exception e) {
            flag=false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 审核纯文本内容
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleTextScan(String content,WmNews wmNews) {
        boolean flag=true;

        if ((wmNews.getTitle()+"-"+content).length()==0){
            return flag;
        }

        try {
            Map<String, String> resText = greenTextScan2.greeTextScan(wmNews.getTitle() + "-" + content);
            if (resText!=null){
                //审核失败
                if (resText.get("result").equals("1")){
                    flag=false;
                    updateWmNews(wmNews,(short)2,"当前文章中存在违规内容");
                }

                //不确定信息需要人工审核
                if (resText.get("result").equals("2")){
                    flag=false;
                    updateWmNews(wmNews, (short) 3,"当前文章中存在不确定内容");
                }
            }
        } catch (Exception e) {
            flag=false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改文章内容
     * @param wmNews
     * @param status
     * @param reason
     */
    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }

    /**
     * 1。从自媒体文章的内容中提取文本和图片
     * 2.提取文章的封面图片
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        //存储纯文本内容
        StringBuilder stringBuilder=new StringBuilder();
        //存储图片内容
        List<String> images=new ArrayList<>();
        //1。从自媒体文章的内容中提取文本和图片
        if (StringUtils.isNotBlank(wmNews.getContent())){
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for (Map map : maps) {
                if (map.get("type").equals("text")){
                    stringBuilder.append(map.get("value"));
                }
                if (map.get("type").equals("image")){
                    images.add((String) map.get("value"));
                }
            }
        }
        //2.提取文章的封面图片
        if (StringUtils.isNotBlank(wmNews.getImages())){
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));
        }

        Map<String,Object> resultMap =new HashMap<>();
        resultMap.put("content",stringBuilder);
        resultMap.put("images",images);
        return resultMap;

    }
}
