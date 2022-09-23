package com.heima.wemedia.common;

import com.heima.apis.article.IArticleClient;
import com.heima.model.article.dots.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SaveAppArticle {
    @Autowired
    private WmChannelMapper channelMapper;

    @Autowired
    private WmUserMapper wmUserMapper;

    @Qualifier("com.heima.apis.article.IArticleClient")
    @Autowired
    private IArticleClient articleClient;
    /**
     * 保存app端相关的文章数据的
     * @param wmNews
     */
    public ResponseResult saveAppArticle(WmNews wmNews) {
        //需要远程调用的一个参数类ArticleDto
        ArticleDto dto = new ArticleDto();
        //属性的拷贝
        BeanUtils.copyProperties(wmNews,dto);
        //文章的布局
        dto.setLayout(wmNews.getType());
        //频道
        WmChannel wmChannel = channelMapper.selectById(wmNews.getChannelId());
        if (wmChannel !=null){
            dto.setChannelName(wmChannel.getName());
        }

        //作者
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if (wmUser!=null){
            dto.setAuthorName(wmUser.getName());
        }

        //设置文章id
        if (wmNews.getArticleId()!=null){
            dto.setId(wmNews.getArticleId());
        }
        dto.setPublishTime(new Date());

        ResponseResult responseResult = articleClient.saveArticle(dto);
        return responseResult;

    }

    public static SaveAppArticle getSaveAppArticle() {
        return new SaveAppArticle();
    }
}
