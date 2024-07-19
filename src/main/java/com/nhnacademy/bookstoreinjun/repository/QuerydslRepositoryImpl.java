package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.cart.CartCheckoutRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.InventoryDecreaseRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.InventoryIncreaseRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.InventorySetRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Cart;
import com.nhnacademy.bookstoreinjun.entity.CartRemoveType;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.QBook;
import com.nhnacademy.bookstoreinjun.entity.QCart;
import com.nhnacademy.bookstoreinjun.entity.QProduct;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.util.FindAllSubCategoriesUtil;
import com.nhnacademy.bookstoreinjun.util.FindAllSubCategoriesUtilImpl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.nhnacademy.bookstoreinjun.entity.QCart.cart;
import static com.nhnacademy.bookstoreinjun.entity.QCartRemoveType.cartRemoveType;
import static com.nhnacademy.bookstoreinjun.entity.QProduct.product;
import static com.nhnacademy.bookstoreinjun.entity.QProductTag.productTag;
import static com.nhnacademy.bookstoreinjun.entity.QTag.tag;
import static com.nhnacademy.bookstoreinjun.entity.QProductCategoryRelation.productCategoryRelation;
import static com.nhnacademy.bookstoreinjun.entity.QProductCategory.productCategory;
import static com.nhnacademy.bookstoreinjun.entity.QProductLike.productLike;
import static com.querydsl.core.types.dsl.Wildcard.count;
import static org.springframework.jdbc.core.JdbcOperationsExtensionsKt.query;


@Slf4j
@Repository
@Transactional(readOnly = true)
public class QuerydslRepositoryImpl extends QuerydslRepositorySupport implements QuerydslRepository {

    private final QProduct p = new QProduct("product");

    private final  QBook b = new QBook("book");

    private final FindAllSubCategoriesUtil findAllSubCategoriesUtil;

    public QuerydslRepositoryImpl(FindAllSubCategoriesUtilImpl findAllSubCategoriesUtil) {
        super(Product.class);
        this.findAllSubCategoriesUtil = findAllSubCategoriesUtil;
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

    private BookProductGetResponseDto makeBookProductGetResponseDto(Tuple tuple, boolean hasProductLike) {
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
                .categorySet(getCategorySet(tuple.get(b.product)))
                .tagSet(getTagSet(tuple.get(b.product)))
                .hasLike(hasProductLike)
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

    private boolean hasProductLike(Long clientId, Long productId) {
        if (clientId != null && clientId != 1){
            Long count = countQuery()
                    .innerJoin(p.productLikes, productLike)
                    .where(p.productId.eq(productId).and(productLike.clientId.eq(clientId)))
                    .fetchOne();
            return count > 0;
        }
        return false;
    }

    private Page<BookProductGetResponseDto> makePage(JPQLQuery<Tuple> query, JPQLQuery<Long> countQuery , Pageable pageable, Long clientId){
        List<Tuple> tupleList = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookProductGetResponseDto> result = new ArrayList<>();
        for (Tuple tuple : tupleList) {
            result.add(makeBookProductGetResponseDto(tuple, hasProductLike(clientId, tuple.get(p.productId))));
        }

        long totalPages = countQuery.fetchOne();
        return new PageImpl<>(result, pageable, totalPages);
    }

    private void addSoldOutChecker(Pageable pageable, BooleanBuilder whereBuilder) {
        Sort sort = pageable.getSort();
        Sort.Order order = sort.iterator().next();
        String property = order.getProperty();

        if (property.equals("product.productInventory")){
            whereBuilder.and(product.productInventory.ne(0L));
        }
    }



    @Transactional
    @Override
    public BookProductGetResponseDto findBookByProductId(Long clientId, Long productId) {
        JPQLQuery<Tuple> query = baseQuery()
                .where(p.productId.eq(productId));

        update(p)
                .set(p.productViewCount, p.productViewCount.add(1))
                .where(p.productId.eq(productId))
                .execute();

        return makeBookProductGetResponseDto(query.fetchOne(), hasProductLike(clientId, productId));
    }

    @Override
    public Page<BookProductGetResponseDto> findAllBookPage(Long clientId, Pageable pageable, Integer productState){
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");
        BooleanBuilder whereBuilder = new BooleanBuilder();

        if(productState != null){
            whereBuilder.and(product.productState.eq(productState));
        }

        addSoldOutChecker(pageable, whereBuilder);

        JPQLQuery<Tuple> query = baseQuery()
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        JPQLQuery<Long> countQuery = countQuery()
                .where(whereBuilder);

        return makePage(query, countQuery, pageable, clientId);
    }

    @Override
    public Page<BookProductGetResponseDto> findNameContainingBookPage(Long clientId, Pageable pageable, String title, Integer productState){
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");
        BooleanBuilder whereBuilder = new BooleanBuilder();



        if(productState != null){
            whereBuilder.and(product.productState.eq(productState));
        }
        whereBuilder.and(b.title.containsIgnoreCase(title));

        JPQLQuery<Tuple> query = baseQuery()
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        JPQLQuery<Long> countQuery = countQuery()
                .where(whereBuilder);

        return makePage(query, countQuery, pageable, clientId);

    }

    @Override
    public Page<BookProductGetResponseDto> findBooksByTagFilter(Long clientId, Set<String> tags, Boolean conditionIsAnd, Pageable pageable, Integer productState) {
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");

        BooleanBuilder whereBuilder = new BooleanBuilder();
        if(productState != null){
            whereBuilder.and(product.productState.eq(productState));
        }

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

        return makePage(query, countQuery, pageable, clientId);
    }

    @Override
    public Page<BookProductGetResponseDto> findBooksByCategoryFilter(Long clientId, Long categoryId, Pageable pageable, Integer productState) {
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");

        Set<String> categoryNameSet = findAllSubCategoriesUtil.getAllSubcategorySet(categoryId).stream()
                .map(ProductCategory::getCategoryName)
                .collect(Collectors.toSet());

        BooleanBuilder whereBuilder = new BooleanBuilder();
        if(productState != null){
            whereBuilder.and(product.productState.eq(productState));
        }
        whereBuilder.and(productCategory.categoryName.in(categoryNameSet));

        JPQLQuery<Tuple> query = baseQuery()
                .innerJoin(p.productCategoryRelations, productCategoryRelation)
                .innerJoin(productCategoryRelation.productCategory, productCategory)
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        JPQLQuery <Long> countQuery = countQuery()
                .innerJoin(p.productCategoryRelations, productCategoryRelation)
                .innerJoin(productCategoryRelation.productCategory, productCategory)
                .where(whereBuilder);

        return makePage(query, countQuery, pageable, clientId);
    }

    @Override
    public Page<BookProductGetResponseDto> findLikeBooks(Long clientId, Pageable pageable, Integer productState) {
        OrderSpecifier<?> orderSpecifier = makeOrderSpecifier(pageable, "book");
        BooleanBuilder whereBuilder = new BooleanBuilder();
        if(productState != null){
            whereBuilder.and(product.productState.eq(productState));
        }
        whereBuilder.and(productLike.clientId.eq(clientId));
        JPQLQuery<Tuple> query = baseQuery()
                .innerJoin(p.productLikes, productLike)
                .where(whereBuilder)
                .orderBy(orderSpecifier);

        JPQLQuery<Long> countQuery = countQuery()
                .innerJoin(p.productLikes, productLike)
                .where(whereBuilder);

        return makePage(query, countQuery, pageable, clientId);
    }

    @Override
    public Set<ProductCategory> getCategorySet(Product realProduct) {
        return Set.copyOf(
                from(p)
                        .select(productCategory)
                        .distinct()
                        .innerJoin(p.productCategoryRelations, productCategoryRelation)
                        .innerJoin(productCategoryRelation.productCategory, productCategory)
                        .where(p.eq(realProduct))
                        .fetch());
    }

    @Override
    public Set<Tag> getTagSet(Product realProduct){
        return Set.copyOf(
                from(p)
                .select(tag)
                .distinct()
                .innerJoin(p.productTags, productTag)
                .innerJoin(productTag.tag, tag)
                .where(p.eq(realProduct))
                .fetch());
    }

    @Transactional
    @Override
    public long setProductState(Long productId, Integer productState) {
        return update(p)
                .set(p.productState, productState)
                .where(p.productId.eq(productId))
                .execute();
    }

    @Transactional
    @Override
    public long decreaseProductInventory(InventoryDecreaseRequestDto inventoryDecreaseRequestDto){

        Map<Long, Long> decreaseRequestMap = inventoryDecreaseRequestDto.decreaseInfo();
        if(decreaseRequestMap == null || decreaseRequestMap.isEmpty()){
            return 0;
        }

        List<Product> products = from(p)
                .select(p)
                .where(p.productId.in(decreaseRequestMap.keySet()))
                .fetch();

        CaseBuilder caseBuilder = new CaseBuilder();
        NumberExpression<Long> caseExpression = p.productInventory;

        for (Product product : products) {
            Long productId = product.getProductId();
            Long currentInventory = product.getProductInventory();
            Long quantityToDecrease = decreaseRequestMap.get(productId);

            if (quantityToDecrease != null) {
                if (currentInventory < quantityToDecrease) {
                    long shortage = quantityToDecrease - currentInventory;
                    log.warn("The inventory of Product (id : {}) is not enough. Shortage : {}, OrderId : {}", productId, shortage, inventoryDecreaseRequestDto.orderId());
                    // 재고가 부족한 경우 0으로 설정
                    caseExpression = caseBuilder
                            .when(p.productId.eq(productId))
                            .then(0L)
                            .otherwise(caseExpression);
                } else {
                    // 재고가 충분한 경우 감소
                    caseExpression = caseBuilder
                            .when(p.productId.eq(productId))
                            .then(p.productInventory.subtract(quantityToDecrease))
                            .otherwise(caseExpression);
                }
            }
        }

        return update(p)
                .set(p.productInventory, caseExpression)
                .where(p.productId.in(decreaseRequestMap.keySet()))
                .execute();
    }


    @Transactional
    @Override
    public long increaseProductInventory(List<InventoryIncreaseRequestDto> dtoList) {
        if(dtoList == null || dtoList.isEmpty()){
            return 0;
        }



        CaseBuilder caseBuilder = new CaseBuilder();
        NumberExpression<Long> caseExpression = p.productInventory;
        for(InventoryIncreaseRequestDto dto : dtoList){
            caseExpression = caseBuilder.when(p.productId.eq(dto.productId())).then(p.productInventory.add(dto.quantityToIncrease())).otherwise(p.productInventory);
        }
        return update(p)
                .set(p.productInventory, caseExpression)
                .where(p.productId.in(dtoList.stream()
                        .map(InventoryIncreaseRequestDto::productId)
                        .toList()))
                .execute();
    }


    @Transactional
    @Override
    public long setProductInventory(InventorySetRequestDto dto) {
        return update(p)
                .set(p.productInventory, dto.quantityToSet())
                .where(p.productId.eq(dto.productId()))
                .execute();
    }

    @Transactional
    @Override
    public boolean checkOutCart(CartCheckoutRequestDto dto) {
        List<Long> productIdList = dto.productIdList();
        if(productIdList == null || productIdList.isEmpty()){
            return false;
        }

        return update(cart)
                .set(cart.cartRemoveType, from(cartRemoveType)
                        .select(cartRemoveType)
                        .where(cartRemoveType.cartRemoveTypeName.eq("구매")))
                .where(cart.clientId.eq(dto.clientId())
                                .and(cart.product.productId.in(dto.productIdList()))
                                .and(cart.cartRemoveType.isNull())
                                )
                .execute() > 0;
    }

    @Transactional
    @Override
    public boolean deleteCartItem(long clientId, long productId) {
        if(clientId <= 0 ||productId <= 0){
            return false;
        }
        return update(cart)
                .set(cart.cartRemoveType, from(cartRemoveType)
                        .select(cartRemoveType)
                        .where(cartRemoveType.cartRemoveTypeName.eq("직접_삭제")))
                .where(cart.clientId.eq(clientId)
                        .and(cart.product.productId.eq(productId))
                        .and(cart.cartRemoveType.isNull())
                )
                .execute() > 0;
    }

    @Transactional
    @Override
    public boolean deleteAllCart(long clientId){
        return update(cart)
                .set(cart.cartRemoveType, from(cartRemoveType)
                        .select(cartRemoveType)
                        .where(cartRemoveType.cartRemoveTypeName.eq("직접_삭제")))
                .where(cart.clientId.eq(clientId)
                        .and(cart.cartRemoveType.isNull())
                )
                .execute() > 0;
    }
}
