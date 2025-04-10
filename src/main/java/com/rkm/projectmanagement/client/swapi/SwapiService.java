package com.rkm.projectmanagement.client.swapi;

import com.rkm.projectmanagement.client.swapi.dto.SwapiResponseDto;
import org.springframework.stereotype.Service;

@Service
public class SwapiService {

    private final SwapiClient swapiClient;

    public SwapiService(SwapiClient swapiClient) {
        this.swapiClient = swapiClient;
    }

    public SwapiResponseDto searchPeopleOnSwapi(String name) {
        return swapiClient.clientSearchPeopleOnSwapi(name);
    }
}
