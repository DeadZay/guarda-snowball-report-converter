package com.github.deadzay.report.mapping;

import com.github.deadzay.report.guarda.GuardaReport;
import com.github.deadzay.report.guarda.Type;
import com.github.deadzay.report.snowball.Event;
import com.github.deadzay.report.snowball.SnowballReport;
import org.mapstruct.*;

import java.text.MessageFormat;

@Mapper
public interface GuardaSnowballMapper {

    @Mappings({
            @Mapping(source = "type", target = "event"),
            @Mapping(target = "symbol", expression = "java(toSymbol(guardaDAO.getCurrencyTo()))"),
            @Mapping(source = "amount", target = "quantity"),
            @Mapping(source = "fee", target = "feeTax")
    })
    SnowballReport guardaToSnowball(GuardaReport guardaDAO);

    @ValueMapping(source = "HASH_LINK_TYPE", target = "Stock_As_Dividend")
    @ValueMapping(source = MappingConstants.ANY_UNMAPPED, target = MappingConstants.NULL)
    Event typeToEvent(Type source);

    default String toSymbol(String currencyTo) {
        return MessageFormat.format("{0}-USD", currencyTo.toUpperCase());
    }

}
