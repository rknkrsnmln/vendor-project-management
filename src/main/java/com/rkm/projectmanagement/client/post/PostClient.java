package com.rkm.projectmanagement.client.post;

import com.rkm.projectmanagement.client.post.dto.CommentDto;
import com.rkm.projectmanagement.client.post.dto.PostDto;

import java.util.List;

public interface PostClient {

    public List<PostDto> clientSearchAllPost();

    public PostDto clientCreatePost(PostDto postDto);

    public PostDto clientFindById(Integer id);

    public PostDto clientUpdatePost(PostDto postDto, Integer id);

    public void clientDelete(Integer id);

    public List<CommentDto> clientFindCommentById(Integer postId);
}
