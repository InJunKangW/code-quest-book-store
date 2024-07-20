package com.nhnacademy.bookstoreinjun.service.product_category_relation;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRelationRepository;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryRelationServiceImpl implements ProductCategoryRelationService {
    private final ProductCategoryRelationRepository productCategoryRelationRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCheckUtil productCheckUtil;

    public void saveProductCategoryRelation(ProductCategoryRelation productCategoryRelation) {
        Product product = productCategoryRelation.getProduct();
        productCheckUtil.checkProduct(product);

        ProductCategory productCategory = productCategoryRelation.getProductCategory();
        if (productCategory == null || productCategory.getProductCategoryId() == null) {
            throw new NotFoundIdException("productCategory", null);
        } else if (!productCategoryRepository.existsById(productCategory.getProductCategoryId())) {
            throw new NotFoundIdException("productCategory", productCategory.getProductCategoryId());
        }
        productCategoryRelationRepository.save(productCategoryRelation);
    }

    public void clearProductCategoryRelationsByProduct(Product product) {
        productCheckUtil.checkProduct(product);
        productCategoryRelationRepository.deleteByProduct(product);
    }
}
