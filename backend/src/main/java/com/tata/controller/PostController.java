package com.tata.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tata.config.AppConstant;
import com.tata.payloads.ApiResponse;
import com.tata.payloads.PostDto;
import com.tata.payloads.PostResponse;
import com.tata.service.FileService;
import com.tata.service.PostService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;

	@PostMapping("/user/{userId}/category/{categoryId}/savePost")
	public ResponseEntity<PostDto> savePost(@Valid @RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {

		PostDto savedPost = this.postService.savePost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(savedPost, HttpStatus.CREATED);
	}

	@GetMapping("/allPosts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam (value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber, 
			@RequestParam (value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam (value = "sortBy", defaultValue = AppConstant.SORT_BY, required = false) String sortBy,
			@RequestParam (value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir
			) {

		return new ResponseEntity<PostResponse>(this.postService.getAllPosts(pageNumber, pageSize, sortBy,sortDir), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable("id") Integer postId) {

		return new ResponseEntity<PostDto>(this.postService.getPostById(postId), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<PostDto> updatePost(@PathVariable("id") Integer postId, @RequestBody PostDto postDto) {

		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable("id") Integer postId) {

		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(LocalDateTime.now(),
				"Post with postId " + postId + " Deleted Successfully...!!", true, null), HttpStatus.OK);
	}

	// get post by userId => (/user/id/posts)

	@GetMapping("/user/{id}/posts")
	public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable("id") Integer userId) {

		return new ResponseEntity<List<PostDto>>(this.postService.getPostByUser(userId), HttpStatus.OK);
	}

	// get post by category id => (/cat/id/post)

	@GetMapping("/category/{id}/posts")
	public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable("id") Integer categoryId) {

		return new ResponseEntity<List<PostDto>>(this.postService.getPostByCategory(categoryId), HttpStatus.OK);
	}

	// search post by key => (string /post)
	
	@GetMapping("/search/{key}/posts")
	public ResponseEntity<List<PostDto>> getPostByKeyword(@PathVariable("key") String keyword) {

		return new ResponseEntity<List<PostDto>>(this.postService.searchPost(keyword), HttpStatus.OK);
	}
	
	
	// image Upload of post the post
		@PostMapping("/post/image/upload/{postId}")
		public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
				@PathVariable Integer postId) throws IOException {

			PostDto postDto = this.postService.getPostById(postId);

			if (postDto == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			String fileName = this.fileService.uploadImage(path, image);

			postDto.setPostImage(fileName);

			return new ResponseEntity<PostDto>(this.postService.updatePost(postDto, postId), HttpStatus.OK);

		}

		// serves file 
//		@GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
//		public void downloadImage(
//		@PathVariable("imageName") String imageName,
//		HttpServletResponse response
//
//		) throws IOException {
//			
//		InputStream resource = this.fileService.getResource(path, imageName);
//		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//		StreamUtils.copy(resource, response.getOutputStream());
//		
//		}
		
		@GetMapping("/post/image/{imageName}")
		public void downloadImage(
		        @PathVariable("imageName") String imageName,
		        HttpServletResponse response
		) throws IOException {

		    // Get file as InputStream
		    InputStream resource = this.fileService.getResource(path, imageName);

		    // Detect file type dynamically
		    String fullPath = path + File.separator + imageName;
		    String contentType = Files.probeContentType(Paths.get(fullPath));

		    // If unable to detect, fallback
		    if (contentType == null) {
		        contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		    }

		    response.setContentType(contentType);

		    // Copy file to response output
		    StreamUtils.copy(resource, response.getOutputStream());
		}


	

}
