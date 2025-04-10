package com.rkm.projectmanagement.client.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.client.post.dto.PostDto;
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

@RestClientTest(PostClient.class)
class PostClientImplTest {

    @Autowired
    private PostClient postClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    private String url;

    @BeforeEach
    void setUp() {
        this.url = "https://jsonplaceholder.typicode.com/posts";
    }

    @Test
    void clientSwapiFindById() throws JsonProcessingException {

        PostDto dataResponseDto = new PostDto(1, 1, "Hello, World!", "This is my first post!");

        String jsonResponse = objectMapper.writeValueAsString(dataResponseDto);
        this.mockServer.expect(MockRestRequestMatchers.requestTo(this.url + "/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        PostDto postDto = this.postClient.clientFindById(1);

        this.mockServer.verify();

        Assertions.assertThat(postDto.getId()).isEqualTo(dataResponseDto.getId());
        Assertions.assertThat(postDto.getTitle()).isEqualTo(dataResponseDto.getTitle());
    }

    @Test
    void clientSwapiFindAll() throws JsonProcessingException {

        List<PostDto> dataResponseDto = List.of(
                new PostDto(1, 1, "Hello, World!", "This is my first post!"),
                new PostDto(2, 1, "Testing Rest Client with @RestClientTest", "This is a post about testing RestClient calls")
        );

        String jsonResponse = objectMapper.writeValueAsString(dataResponseDto);
        this.mockServer.expect(MockRestRequestMatchers.requestTo(this.url))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        List<PostDto> postDtos = this.postClient.clientSearchAllPost();

        this.mockServer.verify();

        Assertions.assertThat(postDtos.getFirst().getId()).isEqualTo(dataResponseDto.getFirst().getId());
        Assertions.assertThat(postDtos.get(1).getTitle()).isEqualTo(dataResponseDto.get(1).getTitle());
    }
}