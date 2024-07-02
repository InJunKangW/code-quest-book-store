package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.QBook;
import com.nhnacademy.bookstoreinjun.entity.QProduct;
import com.nhnacademy.bookstoreinjun.entity.QProductCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.nhnacademy.bookstoreinjun.entity.QProduct.product;
import static com.nhnacademy.bookstoreinjun.entity.QProductTag.productTag;
import static com.nhnacademy.bookstoreinjun.entity.QTag.tag;
import static com.nhnacademy.bookstoreinjun.entity.QProductCategoryRelation.productCategoryRelation;
import static com.nhnacademy.bookstoreinjun.entity.QProductCategory.productCategory;
import static com.querydsl.core.types.dsl.Wildcard.count;
import static com.querydsl.jpa.JPAExpressions.select;


@Slf4j
@Repository
@Transactional(readOnly = true)
public class BookQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookQuerydslRepository {

    private final QProduct p = new QProduct("product");

    private final  QBook b = new QBook("book");

    public BookQuerydslRepositoryImpl() {
        super(Product.class);
    }

    private JPQLQuery<Tuple> baseQuery(){
        return from(b)
                .select(b.bookId, b.title, b.publisher, b.author, b.author, b.isbn, b.isbn13, b.pubDate,
                        b.product, p.productId, p.productThumbnailUrl, p.productName, p.productPackable,
                        p.productDescription, p.productRegisterDate, p.productState, p.productViewCount,
                        p.productPriceStandard, p.productPriceSales, p.productInventory)
                .distinct()
                .innerJoin(b.product, p);
    }

    private JPQLQuery<Long> countQuery(){
        return from(b)
                .select(count)
                .innerJoin(b.product, p);
    }

    private OrderSpecifier<?> makeOrderSpecifier(Pageable pageable, String entity) {
        Sort sort = pageable.getSort();
        Sort.Order order = sort.iterator().next();
        String property = order.getProperty();
        Order orderDirect = order.isDescending()? Order.DESC : Order.ASC;

        return new OrderSpecifier<>(orderDirect, Expressions.stringTemplate(entity + "." + property));
    }

    private BookProductGetResponseDto makeBookProductGetResponseDto(Tuple tuple) {
        return BookProductGetResponseDto.builder()
                .bookId(tuple.get(b.bookId))
                .title(tuple.get(b.title))
                .publisher(tuple.get(b.publisher))
                .author(tuple.get(b.author))
                .pubDate(tuple.get(b.pubDate))
                .isbn(tuple.get(b.isbn))
                .isbn13(tuple.get(b.isbn13))
                .productId(tuple.get(p.productId))
                .cover(tuple.get(p.productThumbnailUrl))
                .productName(tuple.get(p.productName))
                .packable(tuple.get(p.productPackable))
                .productDescription(tuple.get(p.productDescription))
                .productRegisterDate(tuple.get(p.productRegisterDate))
                .productState(tuple.get(p.productState))
                .productViewCount(tuple.get(p.productViewCount))
                .productPriceStandard(tuple.get(p.productPriceStandard))
                .productPriceSales(tuple.get(p.productPriceSales))
                .productInventory(tuple.get(p.productInventory))
                .categories(getAllProductCategoryName(tuple.get(b.product)))
                .tags(getAllTagName(tuple.get(b.product)))
                .build();
    }

    private void makeFilter(JPQLQuery<Tuple> query, JPQLQuery<Long> countQuery ,Boolean conditionIsAnd, int filterSize){
        if(conditionIsAnd){
            query.groupBy(b.bookId)
                    .having(count.eq((long)filterSize));
            countQuery.groupBy(b.bookId)
                    .having(count.eq((long)filterSize));
        }
    }

    private Page<BookProductGetResponseDto> makePage(JPQLQuery<Tuple> query, JPQLQuery<Long> countQuery , Pageable pageable){
        List<Tuple> tupleList = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookProductGetResponseDto> result = new ArrayList<>();
        for (Tuple tuple : tupleList) {
            result.add(makeBookProductGetResponseDto(tuple));
        }

        long totalPages = countQuery.fetchOne();
        return new PageImpl<>(result, pageable, totalPages);
    }


    @Transactional
    @Override
    public BookProductGetResponseDto findBookByBookId(Long bookId) {
        JPQLQuery<Tuple> query = baseQuery()
                .where(b.bookId.eq(bookId));

        update(p)
                .set(p.productViewCount, p.productViewCount.add(1))
                .where(p.productId.eq(
                        from(b)
                                .select(b.product.productId)
                                .where(b.bookId.eq(bookId))))
                .execute();

        return makeBookProductGetResponseDto(query.fetchOne());
    }

    @Override
    public Page<BookProductGetResponseDto> findAllBookPage(Pageable pageable, int productState){
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");
        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(productState));

        JPQLQuery<Tuple> query = baseQuery()
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        JPQLQuery<Long> countQuery = countQuery()
                .where(whereBuilder);

        return makePage(query, countQuery, pageable);
    }

    @Override
    public Page<BookProductGetResponseDto> findNameContainingBookPage(Pageable pageable, String title, int productState){
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");
        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(productState));
        whereBuilder.and(b.title.containsIgnoreCase(title));

        JPQLQuery<Tuple> query = baseQuery()
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        JPQLQuery<Long> countQuery = countQuery()
                .where(whereBuilder);

        return makePage(query, countQuery, pageable);

    }

    @Override
    public Page<BookProductGetResponseDto> findBooksByTagFilter(Set<String> tags, Boolean conditionIsAnd, Pageable pageable) {
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");

        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(0));
        whereBuilder.and(tag.tagName.in(tags));

        JPQLQuery<Tuple> query = baseQuery()
                .innerJoin(p.productTags, productTag)
                .innerJoin(productTag.tag, tag)
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        JPQLQuery <Long> countQuery = countQuery()
                .innerJoin(p.productTags, productTag)
                .innerJoin(productTag.tag, tag)
                .where(whereBuilder);

        makeFilter(query, countQuery, conditionIsAnd, tags.size());

        return makePage(query, countQuery, pageable);
    }

    public Page<BookProductGetResponseDto> findBooksByCategoryFilter(Set<String> categories, Boolean conditionIsAnd, Pageable pageable) {
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");

        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(0));
        whereBuilder.and(productCategory.categoryName.in(categories));

        JPQLQuery<Tuple> query = baseQuery()
                .innerJoin(p.productCategoryRelations, productCategoryRelation)
                .innerJoin(productCategoryRelation.productCategory, productCategory)
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        JPQLQuery <Long> countQuery = countQuery()
                .innerJoin(p.productCategoryRelations, productCategoryRelation)
                .innerJoin(productCategoryRelation.productCategory, productCategory)
                .where(whereBuilder);

        makeFilter(query, countQuery, conditionIsAnd, categories.size());

        return makePage(query, countQuery, pageable);
    }

    public List<String> getAllTagName(Product realProduct){
        return from(p)
                .select(tag.tagName)
                .distinct()
                .innerJoin(p.productTags, productTag)
                .innerJoin(productTag.tag, tag)
                .where(p.eq(realProduct))
                .fetch();
    }

    public List<String> getAllProductCategoryName(Product realProduct) {
        QProductCategory pc = new QProductCategory("productCategory");
        return from(p)
                .select(pc.categoryName)
                .distinct()
                .innerJoin(p.productCategoryRelations, productCategoryRelation)
                .innerJoin(productCategoryRelation.productCategory, pc)
                .where(p.eq(realProduct))
                .fetch();
    }
}
