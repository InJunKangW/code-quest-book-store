package com.nhnacademy.bookstoreinjun.category.entity;

import com.nhnacademy.bookstoreinjun.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testSaveCategory() {
        Category category = Category.builder()
                .categoryName("test category1")
                .build();

        Category savedCategory = entityManager.merge(category);

        assertNotNull(savedCategory);
        assertEquals("test category1", savedCategory.getCategoryName());
        assertNull(savedCategory.getParentCategory());
    }

    @Test
    public void testSaveCategory2() {
        Category parentCategory = Category.builder()
                .categoryName("test parent category1")
                .build();

        entityManager.persist(parentCategory);
        entityManager.flush();

        Category category = Category.builder()
                .categoryName("test category1")
                .parentCategory(parentCategory)
                .build();

        Category savedCategory = entityManager.merge(category);
        assertNotNull(savedCategory);
        assertEquals("test category1", savedCategory.getCategoryName());
        assertEquals(parentCategory, savedCategory.getParentCategory());
    }

    @Test
    public void testUpdateCategory() {
        Category category = Category.builder()
                .categoryName("test category1")
                .build();

        Category savedCategory = entityManager.merge(category);

        savedCategory.setCategoryName("test category2");

        entityManager.flush();

        Category updatedCategory = entityManager.merge(savedCategory);

        assertEquals("test category2", updatedCategory.getCategoryName());
    }
}
