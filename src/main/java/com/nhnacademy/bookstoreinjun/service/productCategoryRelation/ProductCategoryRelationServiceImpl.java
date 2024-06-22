package com.nhnacademy.bookstoreinjun.service.productCategoryRelation;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryRelationServiceImpl implements ProductCategoryRelationService {
    private final ProductCategoryRepository productCategoryRepository;

    private final CategoryRepository categoryRepository;

    public void saveProductCategory(ProductCategoryRelation productCategoryRelation) {
        Product product = productCategoryRelation.getProduct();
        ProductCheckUtil.checkProduct(product);

        ProductCategory productCategory = productCategoryRelation.getProductCategory();
        if (productCategory == null || productCategory.getProductCategoryId() == null) {
            throw new RuntimeException();
        } else if (!categoryRepository.existsById(productCategory.getProductCategoryId())) {
            throw new NotFoundIdException("productCategory", productCategory.getProductCategoryId());
        }
        productCategoryRepository.save(productCategoryRelation);
    }

    public void clearCategoriesByProduct(Product product) {
        productCategoryRepository.deleteByProduct(product);
    }

    public List<ProductCategory> getCategoriesByProduct(Product product) {
        ProductCheckUtil.checkProduct(product);

        List<ProductCategory> result = new ArrayList<>();
        List<ProductCategoryRelation> productCategoryRelations= productCategoryRepository.findByProduct(product);
        for (ProductCategoryRelation productCategoryRelation : productCategoryRelations){
            result.add(productCategoryRelation.getProductCategory());
        }
        return result;
    }
}
