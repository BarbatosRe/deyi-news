package com.heima.model.admin.dtos;

import lombok.Data;

@Data
public class ChannelDto {
    /**
     * 频道名称
     */
    private String name;
    /**
     * 当前页
     */
    private Integer page;
    /**
     * 每页显示条数
     */
    private Integer size;

    public void checkParam(){
        if (this.page<0 || this.page==null){
            setPage(1);
        }
        if (this.size <0 || this.size>100 || this.size==null){
            setSize(10);
        }
    }
}
