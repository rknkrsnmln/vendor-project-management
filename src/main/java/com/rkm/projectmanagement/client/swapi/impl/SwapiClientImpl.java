package com.rkm.projectmanagement.client.swapi.impl;

import com.rkm.projectmanagement.client.swapi.SwapiClient;
import com.rkm.projectmanagement.client.swapi.dto.SwapiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SwapiClientImpl implements SwapiClient {

    private final RestClient restClient;

    public SwapiClientImpl(@Value("${api.rest-client-swapi}") String endPoint,
                           RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(endPoint)
                .build();
    }

    @Override
    public SwapiResponseDto clientSearchPeopleOnSwapi(String name) {
        return this.restClient
                .get()
                .uri("/people/?search={name}", name)
                .retrieve()
                .body(SwapiResponseDto.class);
    }
}
