package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dots.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle>{
    /**
     *
     * @param dto
     * @param type 1 加载更多 2加载最新
     * @return
     */
    public List<ApArticle> loadArticleList(@Param("dto")ArticleHomeDto dto, @Param("type")Short type);
}
