package com.github.deadzay.report.snowball;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class SnowballDAO {

    @JsonProperty("Event")
    private Event event;
    @JsonProperty("Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @JsonProperty("Symbol")
    private String symbol;
    @JsonProperty("Price")
    private Double price;
    @JsonProperty("Quantity")
    private Double quantity;
    @JsonProperty("Currency")
    private String currency = "USD";
    @JsonProperty("FeeTax")
    private Double feeTax = 0D;
    @JsonProperty("Exchange")
    private String exchange;
    @JsonProperty("NKD")
    private Double nkd;
    @JsonProperty("FeeCurrency")
    private String feeCurrency = "USD";
    @JsonProperty("DoNotAdjustCash")
    private Boolean doNotAdjustCash;
    @JsonProperty("Note")
    private String note;

}
