package com.majlo.antares.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majlo.antares.model.location.Sector;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class CHPointsListConverter implements AttributeConverter<List<Sector.CHPoints>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Sector.CHPoints> points) {
        try {
            return objectMapper.writeValueAsString(points);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting list of points to JSON", e);
        }
    }

    @Override
    public List<Sector.CHPoints> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Sector.CHPoints>>() {});
        }
        catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
