package com.nhnacademy.bookstoreinjun.service.productTag;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import java.util.List;

public interface ProductTagService {
    void saveProductTag(ProductTag productTag);
    void clearTagsByProduct(Product product);
    List<Tag> getTagsByProduct(Product product);
}
