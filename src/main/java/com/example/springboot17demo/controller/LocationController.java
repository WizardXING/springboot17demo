package com.example.springboot17demo.controller;

import com.example.springboot17demo.model.LocationData;
import com.example.springboot17demo.service.datasource.LocationDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationDataSource locationDataSource;

    @GetMapping
    public List<LocationData> getAllLocations() {
        return locationDataSource.getAllLocations();
    }

    @GetMapping("/grouped-by-city")
    public Map<String, List<LocationData>> getLocationsByCity() {
        List<LocationData> locations = locationDataSource.getAllLocations();
        return locations.stream()
                .collect(Collectors.groupingBy(LocationData::getCity));
    }

    @GetMapping("/source-type")
    public String getDataSourceType() {
        return locationDataSource.getSourceType();
    }
}