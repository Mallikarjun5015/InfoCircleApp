package com.tata.service;

import java.util.List;

import com.tata.payloads.CategoryDto;

public interface CategoryService {

	CategoryDto saveCategory(CategoryDto categoryDto);

	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);

	public void deleteCategory(Integer categeoryId);

	CategoryDto getCategoryById(Integer categoryId);
	
	CategoryDto getCategoryByTitle(String categoryTitle);

	List<CategoryDto> getAllCategories();
}
