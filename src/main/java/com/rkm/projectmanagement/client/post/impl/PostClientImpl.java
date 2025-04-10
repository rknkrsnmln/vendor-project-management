package com.rkm.projectmanagement.client.post.impl;

import com.rkm.projectmanagement.client.post.PostClient;
import com.rkm.projectmanagement.client.post.dto.CommentDto;
import com.rkm.projectmanagement.client.post.dto.PostDto;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PostClientImpl implements PostClient {

    private final RestClient restClient;

    public PostClientImpl(@Value("${api.rest-client-baseurl}") String endPoint,
                          RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(endPoint)
                .build();
    }

    @Override
    public List<PostDto> clientSearchAllPost() {
        return this.restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public PostDto clientCreatePost(PostDto postDto) {
        return this.restClient.post()
                .uri("/posts")
                .body(postDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public PostDto clientFindById(Integer id) {
        return this.restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(),
                        ((request, response) -> {
                            throw new ObjectNotFoundException("Post", id);
                        }))
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public PostDto clientUpdatePost(PostDto postDto, Integer id) {
        return this.restClient.put()
                .uri("/posts/{id}", id)
                .body(postDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void clientDelete(Integer id) {
        this.restClient.delete()
                .uri("/posts/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public List<CommentDto> clientFindCommentById(Integer postId) {
        return this.restClient.get()
                .uri("/comments?postId={id}", postId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
