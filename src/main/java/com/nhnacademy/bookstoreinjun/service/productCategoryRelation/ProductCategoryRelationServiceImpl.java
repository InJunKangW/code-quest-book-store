package com.nhnacademy.bookstoreinjun.service.productCategoryRelation;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRelationRepository;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import java.util.ArrayList;
import java.util.List;
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
            throw new RuntimeException();
        } else if (!productCategoryRepository.existsById(productCategory.getProductCategoryId())) {
            throw new NotFoundIdException("productCategory", productCategory.getProductCategoryId());
        }
        productCategoryRelationRepository.save(productCategoryRelation);
    }

    public void clearProductCategoryRelationsByProduct(Product product) {
        productCheckUtil.checkProduct(product);
        productCategoryRelationRepository.deleteByProduct(product);
    }

    public List<ProductCategory> getProductCategoryRelationsByProduct(Product product) {
        productCheckUtil.checkProduct(product);

        List<ProductCategory> result = new ArrayList<>();
        List<ProductCategoryRelation> productCategoryRelations= productCategoryRelationRepository.findByProduct(product);
        for (ProductCategoryRelation productCategoryRelation : productCategoryRelations){
            result.add(productCategoryRelation.getProductCategory());
        }
        return result;
    }
}
