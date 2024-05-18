package com.smartbudget.repository;

import com.smartbudget.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByUserId(Long UserId);

    Category findCategoryByIdAndUserId(Long categoryId, Long UserId);

    void deleteCategoryByIdAndUserId(Long categoryId, Long UserId);
}
