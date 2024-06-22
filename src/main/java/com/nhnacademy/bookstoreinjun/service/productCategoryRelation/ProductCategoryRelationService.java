package com.nhnacademy.bookstoreinjun.service.productCategoryRelation;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import java.util.List;

public interface ProductCategoryRelationService {
    void saveProductCategory(ProductCategoryRelation productCategoryRelation);
    void clearCategoriesByProduct(Product product);
    List<ProductCategory> getCategoriesByProduct(Product product);
}
