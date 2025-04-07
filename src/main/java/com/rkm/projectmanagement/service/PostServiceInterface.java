package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.dtos.PostDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

public interface PostServiceInterface {

    @GetExchange("/posts")
    List<PostDto> findAllPosts();

    @GetExchange("/posts/{id}")
    PostDto findById(@PathVariable Integer id);

    @PostExchange("/posts")
    PostDto create(@RequestBody PostDto post);

    @PutExchange("/posts/{id}")
    PostDto updatePost(@RequestBody PostDto post, @PathVariable Integer id);

    @DeleteMapping("/posts/{id}")
    void delete(@PathVariable Integer id);

}
