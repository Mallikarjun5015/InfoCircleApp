package com.tata.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tata.entity.Comment;
import com.tata.entity.Post;
import com.tata.exception.ResourceNotFoundException;
import com.tata.payloads.CommentDto;
import com.tata.repo.CommentRepository;
import com.tata.repo.PostRepository;
import com.tata.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired 
	private PostRepository postRepository;
	
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto saveComment(CommentDto commentDto,Integer postId) {
    	
    	Post post= this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "PostId", postId));
    	
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        
        return modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->new ResourceNotFoundException("Comment", "commentId", commentId));
        return modelMapper.map(comment, CommentDto.class);
    }


}