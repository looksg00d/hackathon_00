package com.smartbudget.service;

import com.smartbudget.DTO.CategoryDTO;
import com.smartbudget.converter.Converter;
import com.smartbudget.model.Category;
import com.smartbudget.repository.CategoryRepository;
import com.smartbudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Converter<Category, CategoryDTO> categoryDTOConverter;

    public List<CategoryDTO> requestCategories(Long userId) {

        return categoryRepository.findCategoriesByUserId(userId).stream()
                .map(categoryDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public CategoryDTO createCategory(String categoryName, Long userId) {

        Category category = new Category();

        category.setName(categoryName);
        category.setUser(userRepository.findUserById(userId));
        categoryRepository.saveAndFlush(category);

        return categoryDTOConverter.convert(category);
    }

    public void removeCategory(Long categoryID, Long userId) {

        categoryRepository.deleteCategoryByIdAndUserId(categoryID, userId);

    }

    public CategoryDTO editCategoryName(String newCategoryName, Long categoryId, Long userId) {

        Category category = categoryRepository.findCategoryByIdAndUserId(categoryId, userId);

        if (category == null) {
            return null;
        }
        category.setName(newCategoryName);
        categoryRepository.saveAndFlush(category);

        return categoryDTOConverter.convert(category);
    }

    public CategoryDTO findCategoryById(Long categoryId, Long userId) {

        return Optional.ofNullable(categoryRepository.findCategoryByIdAndUserId(categoryId, userId))
                .map(categoryDTOConverter::convert)
                .orElse(null);
    }
}
