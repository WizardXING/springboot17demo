package com.example.springboot17demo.mapper;

import com.example.springboot17demo.model.LocationData;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LocationMapper {
    /**
     * 获取所有位置数据
     * @return 位置数据列表
     */
    List<LocationData> getAllLocations();
}