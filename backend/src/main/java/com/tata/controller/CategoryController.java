package com.tata.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tata.payloads.ApiResponse;
import com.tata.payloads.CategoryDto;
import com.tata.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/saveCategory")
	public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestBody CategoryDto categoryDto) {

		CategoryDto saveCategoryDto = this.categoryService.saveCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(saveCategoryDto, HttpStatus.CREATED);
	}

	@GetMapping("/allCategories")
	public ResponseEntity<List<CategoryDto>> getCategory() {

		return new ResponseEntity<List<CategoryDto>>(this.categoryService.getAllCategories(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Integer categoryId) {

		return new ResponseEntity<CategoryDto>(this.categoryService.getCategoryById(categoryId), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") Integer categoryId,
			@Valid @RequestBody CategoryDto categoryDto) {

		CategoryDto updateCategoryDto = this.categoryService.updateCategory(categoryDto, categoryId);

		return new ResponseEntity<CategoryDto>(updateCategoryDto, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") Integer categoryId) {
		this.categoryService.deleteCategory(categoryId);

		return new ResponseEntity<ApiResponse>(
				new ApiResponse(LocalDateTime.now(),"Category with categoryId " + categoryId + " Deleted Successfully...!!", true, null),
				HttpStatus.OK);
	}
	
	@GetMapping("/title/{category}")
	public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable("category") String categoryTitle) {

		return new ResponseEntity<CategoryDto>(this.categoryService.getCategoryByTitle(categoryTitle), HttpStatus.OK);
	}
	
//	@GetMapping("/title/{categoryTitle}")
//	public ResponseEntity<CategoryDto> getCategoryByTitle(
//	        @PathVariable String categoryTitle) {
//
//	    CategoryDto categoryDto = this.categoryService
//	            .getCategoryByTitle(categoryTitle);
//
//	    return ResponseEntity.ok(categoryDto);
//	}

}
