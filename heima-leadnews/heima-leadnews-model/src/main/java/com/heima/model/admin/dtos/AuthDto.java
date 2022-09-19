package com.heima.model.admin.dtos;

import lombok.Data;

@Data
public class AuthDto {

    /**
     * id
     */
    private Integer id;

    /**
     *
     */
    private String msg;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 每页显示条数
     */
    private Integer size;
    /**
     * 状态
     *             0 创建中
     *             1 待审核
     *             2 审核失败
     *             9 审核通过
     */
    private Integer status;


    public void checkParam(){
        if (this.page==null || this.page<0){
            setPage(1);
        }
        if (this.size == null|| this.size<0 ||this.size>100){
            setSize(10);
        }
    }
}
