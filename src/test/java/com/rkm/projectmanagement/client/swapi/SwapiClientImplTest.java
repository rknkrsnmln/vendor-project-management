package com.rkm.projectmanagement.client.swapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.client.swapi.dto.ResultDto;
import com.rkm.projectmanagement.client.swapi.dto.SwapiResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RestClientTest(SwapiClient.class)
class SwapiClientImplTest {

    @Autowired
    private SwapiClient swapiClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    private String url;

    @BeforeEach
    void setUp() {
        this.url = "https://swapi.py4e.com/api/people/?search=luke";
    }

    @Test
    void clientSearchPeopleOnSwapiSuccess() throws JsonProcessingException {

        SwapiResponseDto swapiResponseDto = new SwapiResponseDto(
                List.of(
                        new ResultDto(1L, "Luke Skywalker", "male", "https://swapi.py4e.com/api/planets/1/", "https://swapi.py4e.com/api/people/1/")
                ));

        String jsonResponse = objectMapper.writeValueAsString(swapiResponseDto);
        this.mockServer.expect(MockRestRequestMatchers.requestTo(this.url))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        SwapiResponseDto clientSearchPeopleOnSwapiResponse = this.swapiClient.clientSearchPeopleOnSwapi("luke");

        this.mockServer.verify();
        Assertions.assertThat(clientSearchPeopleOnSwapiResponse.getResults().getFirst().getName()).isEqualTo("Luke Skywalker");
    }
}