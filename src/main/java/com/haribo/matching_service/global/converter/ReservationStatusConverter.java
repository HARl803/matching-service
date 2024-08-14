package com.haribo.matching_service.global.converter;

import com.haribo.matching_service.global.enums.ReservationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ReservationStatusConverter implements AttributeConverter<ReservationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ReservationStatus status) {
        if (status == null) {
            return null;
        }
        return status.getKorean();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(String korean) {
        if (korean == null) {
            return null;
        }

        return Stream.of(ReservationStatus.values())
                .filter(c -> c.getKorean().equals(korean))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

