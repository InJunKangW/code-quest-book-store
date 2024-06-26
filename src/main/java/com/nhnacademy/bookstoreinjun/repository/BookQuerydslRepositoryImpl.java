package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.QProductCategory;
import com.nhnacademy.bookstoreinjun.entity.QProductTag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.util.FindAllSubCategoriesUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.nhnacademy.bookstoreinjun.entity.QBook.book;
import static com.nhnacademy.bookstoreinjun.entity.QProduct.product;
import static com.nhnacademy.bookstoreinjun.entity.QProductTag.productTag;
import static com.nhnacademy.bookstoreinjun.entity.QTag.tag;

@Slf4j
@Repository
@Transactional(readOnly = true)
public class BookQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookQuerydslRepository {

    private final FindAllSubCategoriesUtil findAllSubCategoriesUtil;

    public BookQuerydslRepositoryImpl(FindAllSubCategoriesUtil findAllSubCategoriesUtil) {
        super(Product.class);
        this.findAllSubCategoriesUtil = findAllSubCategoriesUtil;
    }

    @Override
    public List<Book> findBooksByTagFilter(Set<String> tags, Boolean conditionIsAnd) {

        List<Book> bookList = new ArrayList<>();

        if (conditionIsAnd) {
            BooleanBuilder whereBuilder = new BooleanBuilder();
            whereBuilder.and(product.productState.eq(0));
            for (String tagName : tags) {
                whereBuilder.and(tag.tagName.eq(tagName));
            }
            bookList = from(book)
                    .distinct()
                    .innerJoin(book.product, product).fetchJoin()
                    .innerJoin(product.productTags, productTag).fetchJoin()
                    .where(whereBuilder)
                    .fetch();
        }else {
            bookList = from(book)
                    .distinct()
                    .innerJoin(book.product, product).fetchJoin()
                    .innerJoin(product.productTags, productTag).fetchJoin()
                    .where(tag.tagName.in(tags))
                    .where(product.productState.eq(0))
                    .fetch();
        }

        return bookList;
    }

    public List<Book> findBooksByCategoryFilter(Set<String> categories, Boolean conditionIsAnd) {

        Set<ProductCategory> productCategorySet = new LinkedHashSet<>();
        for (String categoryName : categories) {
            findAllSubCategoriesUtil.findAllSubcategories(categoryName, productCategorySet);
        }

        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(0));

        if (conditionIsAnd) {
            for (ProductCategory category : productCategorySet) {
                whereBuilder.and(product.productCategoryRelations.any().productCategory.eq(category));
            }
        } else {
            whereBuilder.and(product.productCategoryRelations.any().productCategory.in(productCategorySet));
        }

        return from(book)
                .innerJoin(book.product, product)
                .where(whereBuilder)
                .distinct()
                .fetch();
    }
}
