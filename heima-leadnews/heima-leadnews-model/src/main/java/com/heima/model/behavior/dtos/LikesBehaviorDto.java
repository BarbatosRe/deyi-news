package com.heima.model.behavior.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class LikesBehaviorDto {

    /**
     * 文章id
     */
    @IdEncrypt
    private long articleId;


    /**
     * 0 点赞 1取消点赞
     *
     */
    private short operation;

    /**
     * 0 文章 1 动态 2 评论
     */
    private short type;
}
