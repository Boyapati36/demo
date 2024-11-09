package com.example.demo.Util;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CstTimeFormatConverter implements AttributeConverter<Instant> {

    private static final ZoneId CST_ZONE = ZoneId.of("America/Chicago");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public AttributeValue transformFrom(Instant applicationTime) {
        // Convert Instant to CST and format it as a String
        ZonedDateTime cstDateTime = applicationTime.atZone(CST_ZONE);
        String cstFormatted = cstDateTime.format(FORMATTER);
        return AttributeValue.builder().s(cstFormatted).build();
    }

    @Override
    public Instant transformTo(AttributeValue dbTime) {
        // Parse the CST time string and convert it to an Instant
        ZonedDateTime cstDateTime = ZonedDateTime.parse(dbTime.s(), FORMATTER.withZone(CST_ZONE));
        return cstDateTime.toInstant();
    }

    @Override
    public EnhancedType<Instant> type() {
        return EnhancedType.of(Instant.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S; // Store the Instant as a String in DynamoDB
    }
}

