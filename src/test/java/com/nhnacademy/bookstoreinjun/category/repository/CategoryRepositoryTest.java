package com.nhnacademy.bookstoreinjun.category.repository;


import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-dev.properties")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    public void setUp() {
        category = Category.builder()
                .categoryName("test category1")
                .build();
    }


    @Test
    public void categorySaveTest() {
        Category savedCategory = categoryRepository.save(category);
        assertNotNull(savedCategory);
        assertEquals(savedCategory.getCategoryName(), "test category1");
        assertNull(savedCategory.getParentCategory());
    }


    @Test
    public void categoryCheckTest() {
        Category savedCategory = categoryRepository.save(category);
        assertNotNull(savedCategory);
        assertTrue(categoryRepository.existsByCategoryName("test category1"));

        Category foundCategory = categoryRepository.findByCategoryName("test category1");
        assertNotNull(foundCategory);
        assertEquals(savedCategory, foundCategory);
    }

    @Test
    public void categoriesCheckTest(){
        Category savedCategory = categoryRepository.save(category);
        for (int i = 0; i < 10; i ++){
            String value = "";
            if(i % 2 == 0){
                value = "add";
            }
            Category subCategory = Category.builder()
                    .categoryName("sub category" + value + i)
                    .parentCategory(savedCategory)
                    .build();
            categoryRepository.save(subCategory);
        }
        List<Category> subCategories = categoryRepository.findSubCategoriesByParent(savedCategory);

        assertNotNull(subCategories);
        assertEquals(subCategories.size(),10);

        List<Category> subContainingCategories = categoryRepository.findAllByCategoryNameContaining("add");
        assertNotNull(subContainingCategories);
        assertEquals(subContainingCategories.size(),5);
    }
}
