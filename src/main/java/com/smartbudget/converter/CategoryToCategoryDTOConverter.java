package com.smartbudget.converter;

import com.smartbudget.DTO.CategoryDTO;
import com.smartbudget.model.Category;
import org.springframework.stereotype.Service;


@Service
public class CategoryToCategoryDTOConverter implements Converter<Category, CategoryDTO> {

    @Override
    public CategoryDTO convert(Category source) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(source.getId());
        categoryDTO.setCategoryName(source.getName());
        categoryDTO.setCustomerId(source.getId());

        return categoryDTO;
    }
}
