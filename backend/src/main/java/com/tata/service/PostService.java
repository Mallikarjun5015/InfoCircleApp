package com.tata.service;

import java.util.List;

import com.tata.payloads.PostDto;
import com.tata.payloads.PostResponse;

public interface PostService {

    PostDto savePost(PostDto postDto, Integer userId, Integer categoryId);

    PostDto updatePost(PostDto postDto, Integer postId);

    void deletePost(Integer postId);

    PostDto getPostById(Integer postId);

    PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    
    List<PostDto> getPostByCategory(Integer categoryId);
    
    List<PostDto> getPostByUser(Integer userId);
    
    List<PostDto> searchPost(String keyword);
}
