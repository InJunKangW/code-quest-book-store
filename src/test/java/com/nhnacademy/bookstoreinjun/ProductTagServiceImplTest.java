package com.nhnacademy.bookstoreinjun;

import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.service.productTag.ProductTagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductTagServiceImplTest {

    @InjectMocks
    private ProductTagServiceImpl productTagServiceImpl;

    @Mock
    private ProductTagRepository productTagRepository;

    @Test
    public void testAddProductTag() {
        ProductTag productTag = new ProductTag();
        productTagServiceImpl.saveProductTag(productTag);
    }

}
