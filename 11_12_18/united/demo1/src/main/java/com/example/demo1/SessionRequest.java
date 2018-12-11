package com.example.demo1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionRequest {
    @JsonProperty("category")
    private String category;

    @JsonProperty("period")
    private String period;

    public String getCategory() {
        return category;
    }

    public String getPeriod() {
        return period;
    }
}
