package com.nhnacademy.bookstoreinjun.category.repository;


import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
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
class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private ProductCategory productCategory;

    @BeforeEach
    public void setUp() {
        productCategory = ProductCategory.builder()
                .categoryName("test category1")
                .build();
    }


    @Test
    void categorySaveTest() {
        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);
        assertNotNull(savedProductCategory);
        assertEquals("test category1", savedProductCategory.getCategoryName());
        assertNull(savedProductCategory.getParentProductCategory());
    }


    @Test
    void categoryCheckTest() {
        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);
        assertNotNull(savedProductCategory);
        assertTrue(productCategoryRepository.existsByCategoryName("test category1"));

        ProductCategory foundProductCategory = productCategoryRepository.findByCategoryName("test category1");
        assertNotNull(foundProductCategory);
        assertEquals(savedProductCategory, foundProductCategory);
    }

}
