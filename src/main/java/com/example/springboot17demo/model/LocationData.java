package com.example.springboot17demo.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LocationData {
    private LocalDate visitDate;        // 访问日期
    private double latitude;           // 纬度
    private double longitude;          // 经度
    private String province;           // 省份
    private String city;               // 城市
    private String district;           // 区
    private String locationName;       // 地点名称
    private String notes;              // 备注
}