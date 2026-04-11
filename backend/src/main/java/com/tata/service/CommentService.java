package com.tata.service;

import java.util.List;
import com.tata.payloads.CommentDto;

public interface CommentService {

    CommentDto saveComment(CommentDto commentDto, Integer postId);

    CommentDto updateComment(CommentDto commentDto, Integer commentId);

	public void deleteComment(Integer commentId);

    CommentDto getCommentById(Integer commentId);

    List<CommentDto> getAllComments();
}