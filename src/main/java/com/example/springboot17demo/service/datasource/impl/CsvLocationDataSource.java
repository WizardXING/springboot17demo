package com.example.springboot17demo.service.datasource.impl;

import com.example.springboot17demo.model.LocationData;
import com.example.springboot17demo.service.datasource.LocationDataSource;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "location.data.source", havingValue = "csv")
public class CsvLocationDataSource implements LocationDataSource {

    @Value("${location.data.csv.path}")
    private String csvFilePath;

    @Override
    public List<LocationData> getAllLocations() {
        List<LocationData> locations = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] headers = reader.readNext(); // 跳过表头
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                LocationData location = new LocationData();
                location.setVisitDate(LocalDate.parse(line[0], formatter));
                location.setLatitude(Double.parseDouble(line[1]));
                location.setLongitude(Double.parseDouble(line[2]));
                location.setProvince(line[3]);
                location.setCity(line[4]);
                location.setDistrict(line[5]);
                location.setLocationName(line[6]);
                location.setNotes(line.length > 7 ? line[7] : "");
                
                locations.add(location);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to read CSV file: " + e.getMessage(), e);
        }

        return locations;
    }

    @Override
    public String getSourceType() {
        return "CSV";
    }
}