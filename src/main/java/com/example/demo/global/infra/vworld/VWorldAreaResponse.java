package com.example.demo.global.infra.vworld;

import java.util.List;

public record VWorldAreaResponse<T>(
        Response<T> response
) {
    public record Response<T>(
            Result<T> result
    ) {}

    public record Result<T>(
            FeatureCollection<T> featureCollection
    ){}

    public record FeatureCollection<T>(
            List<Feature<T>> features
    ){}

    public record Feature<T>(
            T properties
    ){}
}