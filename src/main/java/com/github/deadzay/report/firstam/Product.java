package com.github.deadzay.report.firstam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Product {
    SBMIXED("Фонд смешанный с выплатой дохода"),
    SBEQUITY("Фонд акций с выплатой дохода"),
    SBWEIGHTED("Фонд взвешенный с выплатой дохода"),
    SBCONSERVATIVE("Фонд консервативный с выплатой дохода"),
    SBBOND("Фонд облигаций с выплатой дохода");

    private final String description;

    public String getTicker() {
        return MessageFormat.format("{0}+", this.name());
    }

    public static Product fromDescription(String description) {
        return Arrays.stream(Product.values())
                .filter(product -> product.getDescription().contentEquals(description))
                .findFirst()
                .orElseThrow();
    }

}
