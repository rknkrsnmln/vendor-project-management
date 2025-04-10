package com.rkm.projectmanagement.client.post;

import com.rkm.projectmanagement.client.post.dto.CommentDto;
import com.rkm.projectmanagement.client.post.dto.PostDto;
import com.rkm.projectmanagement.entities.Post;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

//    private final RestClient restClient;

    private final ModelMapper modelMapper;

    private final PostClient postClient;

    private final PostRepository postRepository;

//    public Page<PostDto> findAllPosts(Pageable pageable) {
//        List<PostDto> listOfPost = restClient.get()
//                .uri("/posts")
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//        System.out.println(listOfPost);
//        return null;
//    }

    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }

    public Page<PostDto> findAllPosts(Integer page, Integer size) {
        Pageable pageRequest = createPageRequestUsing(page, size);
        List<PostDto> listOfPost = postClient.clientSearchAllPost();
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), listOfPost.size());

        List<PostDto> pageContent = listOfPost.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, listOfPost.size());
    }

    public List<PostDto> findAllPosts() {
        return postClient.clientSearchAllPost();
    }

    public PostDto create(PostDto postDto) {
        return postClient.clientCreatePost(postDto);
    }

    public PostDto findById(Integer id) {
        return postClient.clientFindById(id);
    }

    public PostDto updatePost(PostDto postDto, Integer id) {
        return postClient.clientUpdatePost(postDto, id);
    }

    public void delete(Integer id) {
        postClient.clientDelete(id);
    }

    public List<CommentDto> findCommentById(Integer postId) {
        return postClient.clientFindCommentById(postId);
    }

    public PostDto addPostById(Integer postId, Integer newId) {
        PostDto postById = postClient.clientFindById(postId);
        System.out.println(postById);
        Post mappedPost = modelMapper.map(postById, Post.class);
        mappedPost.setId(newId);
        System.out.println(mappedPost);
        Post savedPost = postRepository.save(mappedPost);
        System.out.println(savedPost);
        return modelMapper.map(savedPost, PostDto.class);
    }

//    public PostDto addPostById(Integer postId) {
//        PostDto postById = this.findById(postId);
//        Post mappedPost = modelMapper.map(postById, Post.class);
//        mappedPost.setId(postId);
//        Post savedPost = postRepository.save(mappedPost);
//        return modelMapper.map(savedPost, PostDto.class);
//    }
}
