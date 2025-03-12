package com.example.springboot17demo.service.datasource.impl;

import com.example.springboot17demo.model.LocationData;
import com.example.springboot17demo.service.datasource.LocationDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "location.data.source", havingValue = "text")
public class TextFileLocationDataSource implements LocationDataSource {

    @Value("${location.data.text.path}")
    private String textFilePath;

    @Override
    public List<LocationData> getAllLocations() {
        List<LocationData> locations = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(textFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // 跳过空行和注释行
                }
                
                String[] parts = line.split("\\t"); // 使用制表符分隔
                if (parts.length < 7) {
                    continue; // 跳过格式不正确的行
                }
                
                LocationData location = new LocationData();
                location.setVisitDate(LocalDate.parse(parts[0].trim(), formatter));
                location.setLatitude(Double.parseDouble(parts[1].trim()));
                location.setLongitude(Double.parseDouble(parts[2].trim()));
                location.setProvince(parts[3].trim());
                location.setCity(parts[4].trim());
                location.setDistrict(parts[5].trim());
                location.setLocationName(parts[6].trim());
                location.setNotes(parts.length > 7 ? parts[7].trim() : "");
                
                locations.add(location);
            }
        } catch (IOException | NumberFormatException | java.time.format.DateTimeParseException e) {
            throw new RuntimeException("Failed to read text file: " + e.getMessage(), e);
        }

        return locations;
    }

    @Override
    public String getSourceType() {
        return "Text File";
    }
}