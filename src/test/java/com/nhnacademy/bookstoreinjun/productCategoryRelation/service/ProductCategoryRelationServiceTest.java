package com.nhnacademy.bookstoreinjun.productCategoryRelation.service;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRelationRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.service.product_category_relation.ProductCategoryRelationServiceImpl;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCategoryRelationServiceTest {

    @InjectMocks
    private ProductCategoryRelationServiceImpl productCategoryRelationService;

    @Mock
    private ProductCategoryRelationRepository productCategoryRelationRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCheckUtil productCheckUtil;

    @Test
    void saveProductCategoryRelationTestSuccess() {
        when(productCategoryRepository.existsById(1L)).thenReturn(true);

        Product product = new Product();
        ProductCategory productCategory = ProductCategory.builder()
                .productCategoryId(1L)
                .build();
        ProductCategoryRelation productCategoryRelation = ProductCategoryRelation.builder()
                .product(product)
                .productCategory(productCategory)
                .build();

        productCategoryRelationService.saveProductCategoryRelation(productCategoryRelation);

        verify(productCategoryRelationRepository, times(1)).save(productCategoryRelation);
        verify(productCategoryRepository, times(1)).existsById(1L);
        verify(productCheckUtil, times(1)).checkProduct(product);
    }

    @Test
    void saveProductCategoryRelationTestFailureByNullProductCategory() {
        Product product = new Product();
        ProductCategoryRelation productCategoryRelation = ProductCategoryRelation.builder()
                .product(product)
                .build();

        assertThrows(RuntimeException.class, () -> productCategoryRelationService.saveProductCategoryRelation(productCategoryRelation));
    }

    @Test
    void saveProductCategoryRelationTestFailureByNotExistingProductCategory() {
        when(productCategoryRepository.existsById(1L)).thenReturn(false);

        Product product = new Product();
        ProductCategory productCategory = ProductCategory.builder()
                .productCategoryId(1L)
                .build();
        ProductCategoryRelation productCategoryRelation = ProductCategoryRelation.builder()
                .product(product)
                .productCategory(productCategory)
                .build();
        assertThrows(NotFoundIdException.class, () -> productCategoryRelationService.saveProductCategoryRelation(productCategoryRelation));
    }

    @Test
    void clearProductCategoryRelationTestSuccess() {
        Product product = new Product();
        productCategoryRelationService.clearProductCategoryRelationsByProduct(product);

        verify(productCheckUtil, times(1)).checkProduct(product);
        verify(productCategoryRelationRepository, times(1)).deleteByProduct(product);
    }
}
