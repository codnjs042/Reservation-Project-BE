package com.example.demo.global.infra.culture;

import com.fasterxml.jackson.annotation.JsonProperty;

record CultureItem(
        @JsonProperty("rstrNm")       String rstrNm,
        @JsonProperty("rstrBhfNm")    String rstrBhfNm,
        @JsonProperty("rstrAsstnNm")  String rstrAsstnNm,
        @JsonProperty("rstrClNm")     String rstrClNm,
        @JsonProperty("rstrRoadAddr") String rstrRoadAddr,
        @JsonProperty("rstrPnu")      String rstrPnu,
        @JsonProperty("rstrLatPos")   String rstrLatPos,
        @JsonProperty("rstrLotPos")   String rstrLotPos
) {}