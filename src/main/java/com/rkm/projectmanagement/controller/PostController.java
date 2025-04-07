package com.rkm.projectmanagement.controller;

import com.rkm.projectmanagement.dtos.CommentDto;
import com.rkm.projectmanagement.dtos.PostDto;
import com.rkm.projectmanagement.dtos.ResultBaseDto;
import com.rkm.projectmanagement.service.PostService;
import com.rkm.projectmanagement.service.PostServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

//    private final PostServiceInterface postService;
//
//    public PostController(PostServiceInterface postService) {
//        this.postService = postService;
//    }

    @GetMapping("/posts")
    public ResponseEntity<ResultBaseDto<List<PostDto>>> findAll() {
        return new ResponseEntity<>(ResultBaseDto.<List<PostDto>>builder()
                .code(HttpStatus.OK.value())
                .message("Success getting all posts")
                .data(postService.findAllPosts())
                .build(), HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto postDtoReturned = postService.create(postDto);
        return new ResponseEntity<PostDto>(PostDto.builder()
                .title(postDtoReturned.getTitle())
                .userId(postDtoReturned.getUserId())
                .id(postDtoReturned.getId())
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/comments")
    public ResponseEntity<ResultBaseDto<List<CommentDto>>> findCommentsById(@RequestParam(name = "id") Integer postId) {
        return new ResponseEntity<>(ResultBaseDto.<List<CommentDto>>builder()
                .code(HttpStatus.OK.value())
                .message("Success getting all comments")
                .data(postService.findCommentById(postId))
                .build(), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> findById(@PathVariable(name = "id") Integer postId) {
        PostDto postDtoReturned = postService.findById(postId);
        return new ResponseEntity<>(postDtoReturned, HttpStatus.OK);
    }

//    @PostMapping("/posts")
//    public ResponseEntity<PostDto> addPostById(@RequestBody PostDto postDto)  {
//        PostDto postDtoReturned = postService.addPostById(postDto.getId());
//        return new ResponseEntity<>(postDtoReturned, HttpStatus.OK);
//    }

    @PutMapping("/posts")
    public ResponseEntity<PostDto> updatePost(
            @RequestBody PostDto postDto,
            @RequestParam(name = "id") Integer postId) {
        PostDto updatedPost = postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Integer id) {
        postService.delete(id);
    }

}
