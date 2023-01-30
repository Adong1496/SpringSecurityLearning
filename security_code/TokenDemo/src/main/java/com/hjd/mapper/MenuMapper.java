package com.hjd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjd.domain.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);
}
