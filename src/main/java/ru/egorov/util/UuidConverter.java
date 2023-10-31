package ru.egorov.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;
import java.util.UUID;

@Converter
public class UuidConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(final UUID entityValue) {
        return Objects.requireNonNull(entityValue).toString();
    }

    @Override
    public UUID convertToEntityAttribute(final String databaseValue) {
        return UUID.fromString(Objects.requireNonNull(databaseValue));
    }
}
