package com.nhnacademy.bookstoreinjun.productTag.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.service.product_tag.ProductTagServiceImpl;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductTagServiceTest {

    @InjectMocks
    private ProductTagServiceImpl productTagServiceImpl;

    @Mock
    private ProductTagRepository productTagRepository;

    @Mock
    private ProductCheckUtil productCheckUtil;

    @Mock
    private TagRepository tagRepository;

    @DisplayName("상품_태그 데이터 저장 성공 테스트")
    @Test
    void saveProductTagTestSuccess() {
        ProductTag productTag = new ProductTag();
        Product product = productTag.getProduct();
        System.out.println(product);
        Tag tag = new Tag(1L, "test tag");
        productTag.setTag(tag);
        when(tagRepository.existsById(1L)).thenReturn(true);
        productTagServiceImpl.saveProductTag(productTag);

        verify(productTagRepository,times(1)).save(productTag);
        verify(tagRepository,times(1)).existsById(1L);
        verify(productCheckUtil,times(1)).checkProduct(any());
    }

    @DisplayName("상품_태그 데이터 저장 실패 테스트 - 잘못된 tag")
    @Test
    void saveProductTagTestFailureByNullTag() {
        ProductTag productTag = new ProductTag();
        assertThrows(RuntimeException.class, () -> productTagServiceImpl.saveProductTag(productTag));
    }

    @DisplayName("상품_태그 데이터 저장 실패 테스트 - 잘못된 tag")
    @Test
    void saveProductTagTestFailureByNotExistingTag() {
        ProductTag productTag = new ProductTag();
        Tag tag = new Tag(1L, "test tag");
        productTag.setTag(tag);
        when(tagRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundIdException.class, () -> productTagServiceImpl.saveProductTag(productTag));
    }

    @DisplayName("상품_태그 초기화 성공 테스트")
    @Test
    void cleatTagByProductTestSuccess(){
        Product product = new Product();
        productTagServiceImpl.clearTagsByProduct(product);

        verify(productCheckUtil,times(1)).checkProduct(product);
        verify(productTagRepository,times(1)).deleteByProduct(product);
    }
}
