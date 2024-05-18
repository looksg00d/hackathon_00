package com.smartbudget.repository;

import com.smartbudget.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByCustomerId(Long customerId);

    Category findCategoryByIdAndCustomerId(Long categoryId, Long customerId);

    void deleteCategoryByIdAndCustomerId(Long categoryId, Long customerId);
}
