package com.example.springboot17demo.service.datasource.impl;

import com.example.springboot17demo.model.LocationData;
import com.example.springboot17demo.service.datasource.LocationDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.example.springboot17demo.mapper.LocationMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "location.data.source", havingValue = "database")
public class DatabaseLocationDataSource implements LocationDataSource {

    @Autowired
    private LocationMapper locationMapper;

    @Override
    public List<LocationData> getAllLocations() {
        return locationMapper.getAllLocations();
    }

    @Override
    public String getSourceType() {
        return "Database";
    }
}