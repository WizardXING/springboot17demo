package com.example.springboot17demo.service.datasource;

import com.example.springboot17demo.model.LocationData;
import java.util.List;

public interface LocationDataSource {
    /**
     * 获取所有位置数据
     * @return 位置数据列表
     */
    List<LocationData> getAllLocations();
    
    /**
     * 获取数据源类型
     * @return 数据源类型描述
     */
    String getSourceType();
}