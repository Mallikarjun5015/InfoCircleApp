package com.tata.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tata.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Optional<Category> findByCategoryTitle(String categoryTitle);

}