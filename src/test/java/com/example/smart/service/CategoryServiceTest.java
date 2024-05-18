package com.example.smart.service;

import com.smartbudget.DTO.CategoryDTO;
import com.smartbudget.converter.Converter;
import com.smartbudget.model.Category;
import com.smartbudget.model.User;
import com.smartbudget.repository.CategoryRepository;
import com.smartbudget.repository.UserRepository;
import com.smartbudget.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService subj;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    Converter<Category, CategoryDTO> categoryToDTOConverter;

    @Test
    public void createCategory_categoryWasNotCreated() {

        Category category = new Category();
        User user = new User();
        user.setEmail("ivanov@gmail.com");

        category.setId(1L);
        category.setName("Зарплата");
        category.setUser(user);

        when(userRepository.findUserById(1L)).thenReturn(user);

        CategoryDTO categoryDTO = subj.createCategory("Зарплата", 1L);

        assertNull(categoryDTO);
        verify(userRepository, times(1)).findUserById(1L);
    }

    @Test
    public void createCategory_categoryWasCreated() {
        Category category = new Category();
        User user = new User();
        user.setEmail("ivanov@gmail.com");

        category.setName("Зарплата");
        category.setUser(user);
        when(userRepository.findUserById(1L)).thenReturn(user);
        when(categoryRepository.saveAndFlush(category)).thenReturn(null);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setCategoryName("Зарплата");
        categoryDTO.setUserId(1);
        when(categoryToDTOConverter.convert(category)).thenReturn(categoryDTO);

        CategoryDTO categoryDTO2 = subj.createCategory("Зарплата", 1L);

        assertNotNull(categoryDTO2);
        assertEquals(categoryDTO2, categoryDTO);

        verify(categoryRepository, times(1)).saveAndFlush(category);
        verify(categoryToDTOConverter, times(1)).convert(category);

    }

    @Test
    public void editCategoryName() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Зарплата");

        when(categoryRepository.saveAndFlush(category)).thenReturn(category);
        when(categoryRepository.findCategoryByIdAndUserId(1L, 1L)).thenReturn(category);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setCategoryName("ЗП");

        when(categoryToDTOConverter.convert(category)).thenReturn(categoryDTO);

        CategoryDTO categoryDTO2 = subj.editCategoryName("ЗП", 1L, 1L);

        assertEquals(categoryDTO, categoryDTO2);

        verify(categoryRepository, times(1)).saveAndFlush(category);
        verify(categoryRepository, times(1)).findCategoryByIdAndUserId(1L, 1L);
        verify(categoryToDTOConverter, times(1)).convert(category);

    }

}