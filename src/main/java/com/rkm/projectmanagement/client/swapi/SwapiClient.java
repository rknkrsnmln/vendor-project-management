package com.rkm.projectmanagement.client.swapi;

import com.rkm.projectmanagement.client.swapi.dto.SwapiResponseDto;

public interface SwapiClient {

    public SwapiResponseDto clientSearchPeopleOnSwapi(String name);
}
