package com.github.deadzay.report.guarda;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GuardaReport {

    @JsonProperty("Type")
    private Type type;
    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty("Date")
    private Date date;
    @JsonProperty("Time")
    private String time;
    @JsonProperty("Network From")
    private String networkFrom;
    @JsonProperty("Currency From")
    private String currencyFrom;
    @JsonProperty("From")
    private String from;
    @JsonProperty("Network To")
    private String networkTo;
    @JsonProperty("Currency To")
    private String currencyTo;
    @JsonProperty("To")
    private String to;
    @JsonProperty("Incoming")
    private Boolean incoming;
    @JsonProperty("Amount")
    private Double amount;
    @JsonProperty("Fee")
    private Double fee;
    @JsonProperty("Hash")
    private String hash;

}
