package com.tata.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tata.payloads.ApiResponse;
import com.tata.payloads.CommentDto;
import com.tata.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("/post/{postId}/saveComment")
	public ResponseEntity<CommentDto> saveComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Integer postId) {
		
		CommentDto savedComment = commentService.saveComment(commentDto,postId);
		
		return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
	}

	@GetMapping("/allComments")
	public ResponseEntity<List<CommentDto>> getAllComments() {
		return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CommentDto> getCommentById(@PathVariable Integer id) {
		return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<CommentDto> updateComment(@PathVariable Integer id, @RequestBody CommentDto commentDto) {

		CommentDto updatedComment = commentService.updateComment(commentDto, id);
		return new ResponseEntity<>(updatedComment, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable("id") Integer commentId) {

		this.commentService.deleteComment(commentId);

		return new ResponseEntity<ApiResponse>(
				new ApiResponse(LocalDateTime.now(),"Comment with commentId " + commentId + " Deleted Successfully...!!", true, null),
				HttpStatus.OK);
	}
	
	
}