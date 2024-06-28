package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.QBook;
import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import com.nhnacademy.bookstoreinjun.util.FindAllSubCategoriesUtil;
import com.nhnacademy.bookstoreinjun.util.MakePageableUtil;
import com.querydsl.core.BooleanBuilder;
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
    public Page<Book> findBooksByTagFilter(Set<String> tags, Boolean conditionIsAnd, Pageable pageable) {
        Sort sort = pageable.getSort();
        Sort.Order order = sort.iterator().next();
        String property = order.getProperty();
        Order orderDirect = order.isDescending()? Order.DESC : Order.ASC;

        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(orderDirect, Expressions.stringTemplate("book." + property));
        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(0));


        if (conditionIsAnd) {
            for (String tagName : tags) {
                whereBuilder.and(tag.tagName.eq(tagName));
            }
        }else {
            whereBuilder.and(tag.tagName.in(tags));
        }

        JPQLQuery<Book> query = from(book)
                .distinct()
                .innerJoin(book.product, product).fetchJoin()
                .innerJoin(product.productTags, productTag).fetchJoin()
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        List<Book> bookList = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalPages = query.fetchCount();

        return new PageImpl<>(bookList, pageable, totalPages);
    }

    public List<Book> findBooksByCategoryFilter(Set<String> categories, Boolean conditionIsAnd) {

        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(0));

        if (conditionIsAnd) {
            for (String categoryName : categories) {
                Set<ProductCategory> subcategorySet = findAllSubCategoriesUtil.getAllSubcategorySet(categoryName);
                whereBuilder.and(product.productCategoryRelations.any().productCategory.in(subcategorySet));
            }
        } else {
            for (String categoryName : categories) {
                Set<ProductCategory> subcategorySet = findAllSubCategoriesUtil.getAllSubcategorySet(categoryName);
                whereBuilder.or(product.productCategoryRelations.any().productCategory.in(subcategorySet));
            }
        }

        return from(book)
                .innerJoin(book.product, product)
                .where(whereBuilder)
                .distinct()
                .fetch();
    }
}
