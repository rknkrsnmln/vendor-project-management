package com.rkm.projectmanagement.service;

import com.fasterxml.classmate.AnnotationOverrides;
import com.rkm.projectmanagement.dtos.CommentDto;
import com.rkm.projectmanagement.dtos.PostDto;
import com.rkm.projectmanagement.entities.Post;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final RestClient restClient;

    private final ModelMapper modelMapper;

//    private final PostRepository postRepository;


    public List<PostDto> findAllPosts() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public PostDto create(PostDto postDto) {
        return restClient.post()
                .uri("/posts")
                .body(postDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public PostDto findById(Integer id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(),
                        ((request, response) -> {
                    throw new ObjectNotFoundException("Post", id);
                }))
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public PostDto updatePost(PostDto postDto, Integer id) {
        return restClient.put()
                .uri("/posts/{id}", id)
                .body(postDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void delete(Integer id) {
        restClient.delete()
                .uri("/posts/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    public List<CommentDto> findCommentById(Integer postId) {
        return restClient.get()
                .uri("/comments?postId={id}", postId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

//    public PostDto addPostById(Integer postId) {
//        PostDto postById = this.findById(postId);
//        Post mappedPost = modelMapper.map(postById, Post.class);
//        mappedPost.setId(postId);
//        Post savedPost = postRepository.save(mappedPost);
//        return modelMapper.map(savedPost, PostDto.class);
//    }
}
