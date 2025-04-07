package com.rkm.projectmanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.dtos.PostDto;
import com.rkm.projectmanagement.repository.PostRepository;
import com.rkm.projectmanagement.system.configuration.ApplicationProperties;
import com.rkm.projectmanagement.system.configuration.BeanConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;

import java.util.List;


@ContextConfiguration(classes = {ApplicationProperties.class, BeanConfiguration.class})
@RestClientTest(value = {PostService.class})
class PostServiceTest {

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    RestClient restClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostService postService;
    @Autowired
    private ApplicationProperties applicationProperties;


    @Test
    void restClientNotNull() {
        Assertions.assertNotNull(this.restClient);
        Assertions.assertNotNull(this.objectMapper);
    }


    @Test
    public void testMakeCall() throws JsonProcessingException {

        List<PostDto> data = List.of(
                new PostDto(1, 1, "Hello, World!", "This is my first post!"),
                new PostDto(2, 1, "Testing Rest Client with @RestClientTest", "This is a post about testing RestClient calls")
        );


//        Mockito.when(restClient.get().uri("/posts")
//                .headers(any()).body(any())
//                .retrieve()
//                .body(List.class))
//                .thenReturn(data);
//        Mockito.when(
//                restClient.get()
//                .uri("/posts")
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {})
//        ).thenReturn(data);
//        BDDMockito.given(this.postService.findAllPosts().restClient.get()
//                .uri("/posts")
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {}))
//                        .willReturn(data);


        this.mockServer
                .expect(MockRestRequestMatchers. requestTo(applicationProperties.getRestClientBaseurl() + "/posts"))
                .andRespond(MockRestResponseCreators.withSuccess(objectMapper.writeValueAsString(data), MediaType.APPLICATION_JSON));

        List<PostDto> allPosts = this.postService.findAllPosts();
        Assertions.assertEquals(2, allPosts.size());
        Assertions.assertEquals(1, allPosts.get(0).getId());
        Assertions.assertEquals(data, allPosts);
    }

}
