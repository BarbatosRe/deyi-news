package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.heima.model.admin.dtos.NewsAuthDto;
import com.heima.model.wemedia.pojos.WmAuthScanVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
public interface WmAuthScanMapper extends BaseMapper<WmAuthScanVO> {

    public IPage<WmAuthScanVO> findlist(IPage<WmAuthScanVO> page, @Param("dto")NewsAuthDto dto);

    public WmAuthScanVO textDetail(@Param("id") int id);
}
