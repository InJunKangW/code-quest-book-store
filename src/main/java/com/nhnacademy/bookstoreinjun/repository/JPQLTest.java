package com.nhnacademy.bookstoreinjun.repository;

import static com.nhnacademy.bookstoreinjun.entity.QBook.book;
import static com.nhnacademy.bookstoreinjun.entity.QProduct.product;
import static com.nhnacademy.bookstoreinjun.entity.QProductCategory.productCategory;
import static com.nhnacademy.bookstoreinjun.entity.QProductCategoryRelation.productCategoryRelation;
import static com.nhnacademy.bookstoreinjun.entity.QProductTag.productTag;
import static com.nhnacademy.bookstoreinjun.entity.QTag.tag;
//import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.ExpressionUtils.as;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.dsl.Wildcard.count;
import static com.querydsl.jpa.JPAExpressions.select;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.QBook;
import com.nhnacademy.bookstoreinjun.entity.QProduct;
import com.nhnacademy.bookstoreinjun.entity.QProductCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class JPQLTest extends QuerydslRepositorySupport {

    public JPQLTest() {
        super(Book.class);
    }

    private OrderSpecifier<?> makeOrderSpecifier(Pageable pageable, String entity) {
        Sort sort = pageable.getSort();
        Sort.Order order = sort.iterator().next();
        String property = order.getProperty();
        Order orderDirect = order.isDescending()? Order.DESC : Order.ASC;

        return new OrderSpecifier<>(orderDirect, Expressions.stringTemplate(entity + property));
    }

//    public Page<BookProductGetResponseDto> findBooksByTagFilter(Set<String> categories, Set<String> tags, Boolean conditionIsAnd, Pageable pageable) {
//        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book.");
//
//        boolean filteredByTag = !(tags == null || tags.isEmpty());
//        boolean filteredByCategory = !(categories == null || categories.isEmpty());


        public void test(){

        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(product.productState.eq(0));

            QBook b = new QBook("book");
            QProduct p = new QProduct("product");
            QProductCategory pc = new QProductCategory("productCategory");
        JPQLQuery<Tuple> query =

                from(b)
                .select(b.bookId, b.title, b.author, product.productId)
                .distinct()
                .innerJoin(b.product, product)
//                .innerJoin(product.productTags, productTag)
//                .innerJoin(productTag.tag, tag)
                .innerJoin(product.productCategoryRelations, productCategoryRelation)
                .innerJoin(productCategoryRelation.productCategory, productCategory)
                .where(whereBuilder)
                .groupBy(b.bookId)
                .having(count.eq(2L));

        log.warn("query : {}", query);

        List<Tuple> tuples =
              query.fetch();

            JPQLQuery<String> query2 =

                    from(p)
                            .select(pc.categoryName)
                            .distinct()
                            .innerJoin(p.productCategoryRelations, productCategoryRelation)
                            .innerJoin(productCategoryRelation.productCategory, productCategory)
                            .where(p.productId.eq(1L));

            List<String> categories = query2.fetch();

            for (String category : categories) {
                System.out.println("category! :" + category);
            }

        for (Tuple tuple : tuples) {
            System.out.println(tuple.get(book.bookId));
        }

//        return null;
    }
}
