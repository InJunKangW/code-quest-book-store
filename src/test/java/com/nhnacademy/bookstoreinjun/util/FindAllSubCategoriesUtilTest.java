package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("하위 카테고리 조회 유틸 테스트")
class FindAllSubCategoriesUtilTest {

    @InjectMocks
    private FindAllSubCategoriesUtilImpl findAllSubCategoriesUtil;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    private ProductCategory productCategory;

    private ProductCategory parentCategory;

    private ProductCategory childCategory1;

    private ProductCategory childCategory2;

    @BeforeEach
    void setUp() {
        parentCategory = ProductCategory.builder().productCategoryId(1L).build();
        productCategory = ProductCategory.builder().productCategoryId(2L).build();
        childCategory1 = ProductCategory.builder().productCategoryId(3L).build();
        childCategory2 = ProductCategory.builder().productCategoryId(4L).build();
    }

    @Test
    void findAllSubCategoriesTest(){
        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(productCategory);

        List<ProductCategory> subProductCategoryList = new ArrayList<>();
        subProductCategoryList.add(childCategory1);
        subProductCategoryList.add(childCategory2);

        List<ProductCategory> emptyList = new ArrayList<>();

        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(productCategoryRepository.findSubCategoriesByParentProductCategory(parentCategory)).thenReturn(productCategoryList);

        when(productCategoryRepository.findById(2L)).thenReturn(Optional.of(productCategory));
        when(productCategoryRepository.findSubCategoriesByParentProductCategory(productCategory)).thenReturn(subProductCategoryList);

        when(productCategoryRepository.findById(3L)).thenReturn(Optional.of(childCategory1));
        when(productCategoryRepository.findSubCategoriesByParentProductCategory(childCategory1)).thenReturn(emptyList);

        when(productCategoryRepository.findById(4L)).thenReturn(Optional.of(childCategory2));
        when(productCategoryRepository.findSubCategoriesByParentProductCategory(childCategory2)).thenReturn(emptyList);

        Set<ProductCategory> allSubCategorySet = findAllSubCategoriesUtil.getAllSubcategorySet(1L);

        assertNotNull(allSubCategorySet);
        assertEquals(4, allSubCategorySet.size());

        verify(productCategoryRepository, times(4)).findById(any());
        verify(productCategoryRepository, times(4)).findSubCategoriesByParentProductCategory(any());
    }

    @Test
    void findAllSubCategoriesTest2(){
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundIdException.class, () -> findAllSubCategoriesUtil.getAllSubcategorySet(1L));
    }

    @Test
    void findAllSubCategoriesTest3(){
        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(productCategory);

        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(productCategoryRepository.findSubCategoriesByParentProductCategory(parentCategory)).thenReturn(productCategoryList);

        when(productCategoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundIdException.class, () -> findAllSubCategoriesUtil.getAllSubcategorySet(1L));
    }
}
