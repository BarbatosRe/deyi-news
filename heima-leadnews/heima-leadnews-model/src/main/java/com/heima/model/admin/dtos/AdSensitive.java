package com.heima.model.admin.dtos;

import lombok.Data;

@Data
public class AdSensitive {
    /**
     * 敏感词id
     */
    private Integer id;
    /**
     * 敏感词
     */
    private String sensitives;
    /**
     *敏感词创建时间
     */
    private String createdTime;
}
