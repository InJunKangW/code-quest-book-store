package com.nhnacademy.bookstoreinjun.category.repository;


import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
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
public class ProductCategoryRepositoryTest {

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
    public void categorySaveTest() {
        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);
        assertNotNull(savedProductCategory);
        assertEquals(savedProductCategory.getCategoryName(), "test category1");
        assertNull(savedProductCategory.getParentProductCategory());
    }


    @Test
    public void categoryCheckTest() {
        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);
        assertNotNull(savedProductCategory);
        assertTrue(productCategoryRepository.existsByCategoryName("test category1"));

        ProductCategory foundProductCategory = productCategoryRepository.findByCategoryName("test category1");
        assertNotNull(foundProductCategory);
        assertEquals(savedProductCategory, foundProductCategory);
    }

    @Test
    public void categoriesCheckTest(){
        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);
        for (int i = 0; i < 10; i ++){
            String value = "";
            if(i % 2 == 0){
                value = "add";
            }
            ProductCategory subProductCategory = ProductCategory.builder()
                    .categoryName("sub productCategory" + value + i)
                    .parentProductCategory(savedProductCategory)
                    .build();
            productCategoryRepository.save(subProductCategory);
        }
        List<ProductCategory> subCategories = productCategoryRepository.findSubCategoriesByParentProductCategory(savedProductCategory);

        assertNotNull(subCategories);
        assertEquals(10, subCategories.size());

        List<ProductCategory> subContainingCategories = productCategoryRepository.findAllByCategoryNameContaining("add");
        assertNotNull(subContainingCategories);
        assertEquals(5, subContainingCategories.size());
    }
}
