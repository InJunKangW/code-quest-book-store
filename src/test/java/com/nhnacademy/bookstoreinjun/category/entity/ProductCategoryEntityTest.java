package com.nhnacademy.bookstoreinjun.category.entity;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-dev.properties")

public class ProductCategoryEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    private final String TEST_CATEGORY_NAME = "Test Category";

    @Test
    public void testSaveCategory() {
        ProductCategory productCategory = ProductCategory.builder()
                .categoryName(TEST_CATEGORY_NAME)
                .build();

        ProductCategory savedProductCategory = entityManager.merge(productCategory);

        assertNotNull(savedProductCategory);
        assertNotNull(savedProductCategory.getProductCategoryId());
        assertEquals(TEST_CATEGORY_NAME, savedProductCategory.getCategoryName());
        assertNull(savedProductCategory.getParentProductCategory());
    }

    @Test
    public void testSaveCategory2() {
        ProductCategory parentProductCategory = ProductCategory.builder()
                .categoryName(TEST_CATEGORY_NAME + "parent")
                .build();

        entityManager.persist(parentProductCategory);
        entityManager.flush();

        ProductCategory productCategory = ProductCategory.builder()
                .categoryName(TEST_CATEGORY_NAME)
                .parentProductCategory(parentProductCategory)
                .build();

        ProductCategory savedProductCategory = entityManager.merge(productCategory);
        assertNotNull(savedProductCategory);
        assertNotNull(savedProductCategory.getProductCategoryId());

        ProductCategory savedParentProductCategory = savedProductCategory.getParentProductCategory();

        assertEquals(parentProductCategory, savedParentProductCategory);
        assertEquals(TEST_CATEGORY_NAME + "parent", savedParentProductCategory.getCategoryName());
    }

    @Test
    public void testUpdateCategory() {
        ProductCategory parentProductCategory = ProductCategory.builder()
                .categoryName(TEST_CATEGORY_NAME + "parent")
                .build();

        entityManager.persist(parentProductCategory);
        entityManager.flush();

        ProductCategory productCategory = ProductCategory.builder()
                .categoryName(TEST_CATEGORY_NAME)
                .parentProductCategory(parentProductCategory)
                .build();

        ProductCategory savedProductCategory = entityManager.merge(productCategory);

        savedProductCategory.setCategoryName("new" + TEST_CATEGORY_NAME);
        parentProductCategory.setCategoryName("new" + TEST_CATEGORY_NAME + "parent");

        entityManager.flush();

        ProductCategory updatedProductCategory = entityManager.merge(savedProductCategory);

        assertNotNull(savedProductCategory.getProductCategoryId());
        assertEquals("new" + TEST_CATEGORY_NAME, updatedProductCategory.getCategoryName());
        assertEquals("new" + TEST_CATEGORY_NAME + "parent", updatedProductCategory.getParentProductCategory().getCategoryName());
    }
}
