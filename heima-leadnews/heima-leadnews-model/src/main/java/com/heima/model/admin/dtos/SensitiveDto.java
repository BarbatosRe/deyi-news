package com.heima.model.admin.dtos;

import lombok.Data;

@Data
public class SensitiveDto {
    /**
     *查询词
     */
    private String name;
    /**
     *当前页
     */
    private Integer page;
    /**
     *每页显示条数
     */
    private Integer size;

    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10);
        }
    }

}
