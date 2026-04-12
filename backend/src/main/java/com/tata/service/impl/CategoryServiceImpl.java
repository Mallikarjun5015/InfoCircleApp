package com.tata.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tata.entity.Category;
import com.tata.exception.ResourceNotFoundException;
import com.tata.payloads.CategoryDto;
import com.tata.repo.CategoryRepository;
import com.tata.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto saveCategory(CategoryDto categoryDto) {

		Category category = this.modelMapper.map(categoryDto, Category.class);
		Category saveCategory = this.categoryRepository.save(category);
		return this.modelMapper.map(saveCategory, CategoryDto.class);

	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<Category> category = this.categoryRepository.findAll();
		List<CategoryDto> categoryDtos = category.stream().map((cat) -> this.modelMapper.map(cat, CategoryDto.class))
				.collect(Collectors.toList());
		return categoryDtos;
	}

	@Override
	public CategoryDto getCategoryById(Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		return this.modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryTagLine(categoryDto.getCategoryTagLine());
		category.setCategoryImage(categoryDto.getCategoryImage());
		category.setCategoryDescription(categoryDto.getCategoryDescription());

		Category updatedCategory = this.categoryRepository.save(category);

		return this.modelMapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {

		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		categoryRepository.delete(category);
	}

	@Override
	public CategoryDto getCategoryByTitle(String categoryTitle) {
		Category category = this.categoryRepository
				.findByCategoryTitle(categoryTitle).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryTitle", categoryTitle));
		
		  return this.modelMapper.map(category, CategoryDto.class);
	}
	
}
