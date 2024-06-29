package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.QBook;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import com.nhnacademy.bookstoreinjun.util.FindAllSubCategoriesUtil;
import com.nhnacademy.bookstoreinjun.util.MakePageableUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.nhnacademy.bookstoreinjun.entity.QBook.book;
import static com.nhnacademy.bookstoreinjun.entity.QProduct.product;
import static com.nhnacademy.bookstoreinjun.entity.QProductTag.productTag;
import static com.nhnacademy.bookstoreinjun.entity.QTag.tag;
import static com.nhnacademy.bookstoreinjun.entity.QProductCategoryRelation.productCategoryRelation;
import static com.nhnacademy.bookstoreinjun.entity.QProductCategory.productCategory;
import static com.querydsl.jpa.JPAExpressions.select;


@Slf4j
@Repository
@Transactional(readOnly = true)
public class BookQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookQuerydslRepository {

    private final FindAllSubCategoriesUtil findAllSubCategoriesUtil;

    private final ProductTagRepository productTagRepository;

    public BookQuerydslRepositoryImpl(FindAllSubCategoriesUtil findAllSubCategoriesUtil,
                                      ProductTagRepository productTagRepository) {
        super(Product.class);
        this.findAllSubCategoriesUtil = findAllSubCategoriesUtil;
        this.productTagRepository= productTagRepository;
    }

    private OrderSpecifier<?> makeOrderSpecifier(Pageable pageable, String entity) {
        Sort sort = pageable.getSort();
        Sort.Order order = sort.iterator().next();
        String property = order.getProperty();
        Order orderDirect = order.isDescending()? Order.DESC : Order.ASC;

        return new OrderSpecifier<>(orderDirect, Expressions.stringTemplate(entity + property));
    }

    @Override
    public Page<BookProductGetResponseDto> findBooksByTagFilter(Set<String> categories, Set<String> tags, Boolean conditionIsAnd, Pageable pageable) {
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book.");

        boolean filteredByTag = !(tags == null || tags.isEmpty());
        boolean filteredByCategory = !(categories == null || categories.isEmpty());

        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(0));

        if (filteredByTag) {
            whereBuilder.and(tag.tagName.in(tags));
        }
        if (conditionIsAnd) {
            if (filteredByCategory) {
                for (String categoryName : categories) {
                    Set<ProductCategory> subcategorySet = findAllSubCategoriesUtil.getAllSubcategorySet(categoryName);
                    whereBuilder.and(product.productCategoryRelations.any().productCategory.in(subcategorySet));
                }
            }
        } else {
            if (filteredByCategory) {
                for (String categoryName : categories) {
                    Set<ProductCategory> subcategorySet = findAllSubCategoriesUtil.getAllSubcategorySet(categoryName);
                    whereBuilder.or(product.productCategoryRelations.any().productCategory.in(subcategorySet));
                }
            }
        }

        log.warn("query making start");

        JPQLQuery<Book> query = from(book)
                .distinct()
                .innerJoin(book.product, product).fetchJoin()
                .innerJoin(product.productTags, productTag).fetchJoin()
                .innerJoin(productTag.tag, tag).fetchJoin()
//                .innerJoin(product.productCategoryRelations, productCategoryRelation).fetchJoin()
//                .innerJoin(productCategoryRelation.productCategory, productCategory).fetchJoin()
                .where(whereBuilder)
                .orderBy(orderSpecifier)
                ;

//        if (conditionIsAnd && filteredByTag) {
//            log.error("quasfadasdsda!");
//           query = query.groupBy(book.bookId);
//        }
        log.error("query made!");


        List<Book> bookList = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.error("bookList made!");

        List<BookProductGetResponseDto> result = new ArrayList<>();
        for (Book realBook : bookList) {
            log.error("product making start");

            Product realProduct = realBook.getProduct();

            log.error("get product done");

            BookProductGetResponseDto  bookProductGetResponseDto= BookProductGetResponseDto.builder()
                    .bookId(realBook.getBookId())
                    .title(realBook.getTitle())
                    .publisher(realBook.getPublisher())
                    .author(realBook.getAuthor())
                    .pubDate(realBook.getPubDate())
                    .isbn(realBook.getIsbn())
                    .isbn13(realBook.getIsbn13())
                    .productId(realProduct.getProductId())
                    .cover(realProduct.getProductThumbnailUrl())
                    .productName(realProduct.getProductName())
                    .packable(realProduct.isProductPackable())
                    .productDescription(realProduct.getProductDescription())
                    .productRegisterDate(realProduct.getProductRegisterDate())
                    .productState(realProduct.getProductState())
                    .productViewCount(realProduct.getProductViewCount())
                    .productPriceStandard(realProduct.getProductPriceStandard())
                    .productPriceSales(realProduct.getProductPriceSales())
                    .productInventory(realProduct.getProductInventory())
                    .categories(
                            filteredByCategory ?
                                    getAllProductCategory(realProduct) :

                                    realProduct.getProductCategoryRelations().stream()
                                            .map(ProductCategoryRelation :: getProductCategory)
                                            .map(ProductCategory :: getCategoryName)
                                            .collect(Collectors.toList()))
                    .tags(
                            filteredByTag ?
                                    getAllTag(realProduct) :

                                    realProduct.getProductTags().stream()
                                            .map(ProductTag ::getTag)
                                            .map(Tag::getTagName)
                                            .collect(Collectors.toList())
                            )
                    .build();

            log.error("product setting done");

            result.add(bookProductGetResponseDto);
        }

        long totalPages = query.fetchCount();

        return new PageImpl<>(result, pageable, totalPages);

//        if (conditionIsAnd) {
//            query = query
//                    .groupBy(book.bookId)
//                    .having(productTag.count().eq((long) tags.size()));
//        }
    }

    public List<Book> findBooksByCategoryFilter(Set<String> categories, Boolean conditionIsAnd) {

        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(0));

//        if (conditionIsAnd) {
//            for (String categoryName : categories) {
//                Set<ProductCategory> subcategorySet = findAllSubCategoriesUtil.getAllSubcategorySet(categoryName);
//                whereBuilder.and(product.productCategoryRelations.any().productCategory.in(subcategorySet));
//            }
//        } else {
//            for (String categoryName : categories) {
//                Set<ProductCategory> subcategorySet = findAllSubCategoriesUtil.getAllSubcategorySet(categoryName);
//                whereBuilder.or(product.productCategoryRelations.any().productCategory.in(subcategorySet));
//            }
//        }

        return from(book)
                .innerJoin(book.product, product)
                .where(whereBuilder)
                .distinct()
                .fetch();
    }

    public List<String> getAllTag(Product realProduct){
        log.error("asd start!");
        return from(productTag)
                .innerJoin(productTag.product, product).fetchJoin()
                .innerJoin(productTag.tag, tag).fetchJoin()
                .where(product.eq(realProduct))
                .fetch()
                .stream()
                .map(ProductTag::getTag)
                .map(Tag::getTagName)
                .toList();
    }

    public List<String> getAllProductCategory(Product realProduct){
        log.error("asd start!1231232");
        return from(productCategoryRelation)
                .innerJoin(productCategoryRelation.product).fetchJoin()
                .where(product.eq(realProduct))
                .fetch()
                .stream()
                .map(ProductCategoryRelation::getProductCategory)
                .map(ProductCategory::getCategoryName)
                .toList();
    }

}
