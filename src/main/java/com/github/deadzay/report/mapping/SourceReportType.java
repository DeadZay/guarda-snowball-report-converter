package com.github.deadzay.report.mapping;

import org.apache.commons.cli.Converter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.Map;

public enum SourceReportType implements Converter<SourceReportType, ParseException> {
    GUARDA,
    FIRSTAM,
    UNKNOWN;

    private static final MultiValuedMap<SourceReportType, String> VALUES = new ArrayListValuedHashMap<>(){{
        for (SourceReportType value : SourceReportType.values()) {
            put(value, value.name());
            put(value, value.name().toLowerCase());
        }
    }};

    @Override
    public SourceReportType apply(String string) {
        return VALUES.entries().stream()
                .filter(entry -> entry.getValue().contentEquals(string))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(UNKNOWN);
    }
}
