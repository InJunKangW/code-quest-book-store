package com.nhnacademy.bookstoreinjun.service.product;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRelationRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.util.MakePageableUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDtoServiceImpl implements ProductDtoService {

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryRelationRepository productCategoryRelationRepository;

    private final TagRepository tagRepository;

    private final ProductTagRepository productTagRepository;

    private final int DEFAULT_PAGE_SIZE = 10;

    private final String DEFAULT_SORT = "productId";

    private ProductGetResponseDto makeProductGetResponseDtoFromProduct(Product product) {
        return ProductGetResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productState(product.getProductState())
                .productPriceStandard(product.getProductPriceStandard())
                .productPriceSales(product.getProductPriceSales())
                .productThumbNailImage(product.getProductThumbnailUrl())
                .build();
    }



    private Page<ProductGetResponseDto> makeProductGetResponseDtoPage(Pageable pageable, Page<Product> productPage) {
        int total = productPage.getTotalPages();
        int maxPage = pageable.getPageNumber() + 1;

        if (total != -0 && total < maxPage){
            throw new PageOutOfRangeException(total, maxPage);
        }

        return productPage.map(this::makeProductGetResponseDtoFromProduct);
    }

    public Page<ProductGetResponseDto> findAllPage(@Valid PageRequestDto pageRequestDto) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<Product> productPage = productRepository.findAll(pageable);
            return makeProductGetResponseDtoPage(pageable, productPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.sortExceptionHandle(pageable);
        }
    }

    public Page<ProductGetResponseDto> findNameContainingPage(@Valid PageRequestDto pageRequestDto, String productName) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<Product> productPage = productRepository.findByProductNameContaining(pageable, productName);
            return makeProductGetResponseDtoPage(pageable, productPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.sortExceptionHandle(pageable);
        }
    }

    private void findAllSubcategories(String parentName, Set<ProductCategory> categoryNames) {
        ProductCategory parentCategory = productCategoryRepository.findByCategoryName(parentName);
        if (parentCategory == null) {
            throw new NotFoundNameException("category", parentName);
        }
        categoryNames.add(parentCategory);

        List<ProductCategory> subcategories = productCategoryRepository.findSubCategoriesByParentProductCategory(parentCategory);
        for (ProductCategory subcategory : subcategories) {
            findAllSubcategories(subcategory.getCategoryName(), categoryNames);
        }
    }

    public Set<Product> findAllByCategories(Set<String> categories, Boolean conditionIsAnd) {
        conditionIsAnd = conditionIsAnd == null || conditionIsAnd;
        boolean isFirstSearch = true;
        Set<Product> result = new LinkedHashSet<>();
        for (String categoryName : categories) {
            Set<Product> productSet = new LinkedHashSet<>();

            Set<ProductCategory> categorySet = new LinkedHashSet<>();
            findAllSubcategories(categoryName, categorySet);

            for (ProductCategory category : categorySet) {
                List<ProductCategoryRelation> productCategoryRelation = productCategoryRelationRepository.findByProductCategory(category);
                for (ProductCategoryRelation relation : productCategoryRelation) {
                    Product product = relation.getProduct();
                    productSet.add(product);
                }
            }

            if(isFirstSearch){
                result.addAll(productSet);
                isFirstSearch = false;
            }else{
                if(conditionIsAnd){
                    result.retainAll(productSet);
                }else{
                    result.addAll(productSet);
                }
            }
        }
        return result;
    }

    public Set<Product> findAllByTags(Set<String> tags, Boolean conditionIsAnd) {
        conditionIsAnd = conditionIsAnd == null || conditionIsAnd;
        boolean isFirstSearch = true;
        for (String tag : tags) {
            log.warn("request tag : {}", tag);
        }
        Set<Product> result = new LinkedHashSet<>();
        for (String tagName : tags) {
            Set<Product> productSet = new LinkedHashSet<>();
            Tag tag = tagRepository.findByTagName(tagName);

            if (tag == null) {
                throw new NotFoundNameException("tag", tagName);
            }

            List<ProductTag> productTag = productTagRepository.findByTag(tag);
                for (ProductTag productTag1 : productTag) {
                    Product product = productTag1.getProduct();
                    productSet.add(product);
                    log.warn("product : {}", product.getProductName());
                }
            if(isFirstSearch){
                result.addAll(productSet);
                isFirstSearch = false;
            }else{
                if(conditionIsAnd){
                    result.retainAll(productSet);
                }else{
                    result.addAll(productSet);
                }
            }
        }
        return result;
    }

}
