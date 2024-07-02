package com.nhnacademy.bookstoreinjun.productCategoryRelation.service;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRelationRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.service.productCategoryRelation.ProductCategoryRelationServiceImpl;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductCategoryRelationServiceTest {

    @InjectMocks
    private ProductCategoryRelationServiceImpl productCategoryRelationService;

    @Mock
    private ProductCategoryRelationRepository productCategoryRelationRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCheckUtil productCheckUtil;

    @Test
    public void saveProductCategoryRelationTestSuccess() {
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
    public void saveProductCategoryRelationTestFailureByNullProductCategory() {
        Product product = new Product();
        ProductCategoryRelation productCategoryRelation = ProductCategoryRelation.builder()
                .product(product)
                .build();

        assertThrows(RuntimeException.class, () -> productCategoryRelationService.saveProductCategoryRelation(productCategoryRelation));
    }

    @Test
    public void saveProductCategoryRelationTestFailureByNotExistingProductCategory() {
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
    public void clearProductCategoryRelationTestSuccess() {
        Product product = new Product();
        productCategoryRelationService.clearProductCategoryRelationsByProduct(product);

        verify(productCheckUtil, times(1)).checkProduct(product);
        verify(productCategoryRelationRepository, times(1)).deleteByProduct(product);
    }

//    @DisplayName("상품에 달린 카테고리 리스트 조회")
//    @Test
//    public void getTagsByProductTestSuccess(){
//        Product product = new Product();
//
//        when(productCategoryRelationRepository.findByProduct(product)).thenReturn(Arrays.asList(
//                ProductCategoryRelation.builder()
//                        .product(product)
//                        .build(),
//                ProductCategoryRelation.builder()
//                        .product(product)
//                        .build()
//        ));
//        List<ProductCategory> productCategoryList = productCategoryRelationService.getProductCategoryRelationsByProduct(product);
//
//        verify(productCategoryRelationRepository,times(1)).findByProduct(product);
//        assertEquals(2, productCategoryList.size());
//    }
}
