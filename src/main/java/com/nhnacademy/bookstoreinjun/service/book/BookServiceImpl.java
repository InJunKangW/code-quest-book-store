package com.nhnacademy.bookstoreinjun.service.book;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.BookQuerydslRepository;
import com.nhnacademy.bookstoreinjun.repository.BookRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.service.productCategoryRelation.ProductCategoryRelationService;
import com.nhnacademy.bookstoreinjun.service.productTag.ProductTagService;
import com.nhnacademy.bookstoreinjun.util.PageableUtil;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookQuerydslRepository querydslRepository;

    private final ProductRepository productRepository;

    private final TagRepository tagRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryRelationService productCategoryRelationService;

    private final ProductTagService productTagService;

    private final ProductCheckUtil productCheckUtil;

    private final String TYPE = "book";

    private final int DEFAULT_PAGE_SIZE = 5;
    private final String DEFAULT_SORT = "product.productRegisterDate";


    private void saveProductCategoryRelation(Set<String> categories, Product product){
        for (String categoryName : categories) {
            ProductCategory productCategory = productCategoryRepository.findByCategoryName(categoryName);
            if (productCategory == null){
                throw new NotFoundNameException("category", categoryName);
            }

            ProductCategoryRelation productCategoryRelation = ProductCategoryRelation.builder()
                    .productCategory(productCategory)
                    .product(product)
                    .build();

            productCategoryRelationService.saveProductCategoryRelation(productCategoryRelation);
        }
    }

    private void saveProductTag(Set<String> tags, Product product){
        if (tags != null){
            for (String tagName : tags) {
                Tag tag = tagRepository.findByTagName(tagName);
                if (tag == null) {
                    throw new NotFoundNameException("tag", tagName);
                }

                ProductTag productTag = ProductTag.builder()
                        .product(product)
                        .tag(tag)
                        .build();
                productTagService.saveProductTag(productTag);
            }
        }
    }


    public BookProductGetResponseDto getBookByBookId(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundIdException(TYPE, bookId);
        }
        try {
            return querydslRepository.findBookByBookId(bookId);
        }catch (NullPointerException e){
            throw new NotFoundIdException(TYPE, bookId);
        }
    }

    public Page<BookProductGetResponseDto> getBookPage(PageRequestDto pageRequestDto) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try{
            Page<BookProductGetResponseDto> result = querydslRepository.findAllBookPage(pageable, 0);

            PageableUtil.pageNumCheck(result, pageable);

            return result;
        }catch (InvalidDataAccessApiUsageException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public Page<BookProductGetResponseDto> getNameContainingBookPage(PageRequestDto pageRequestDto, String title) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        try{
            Page<BookProductGetResponseDto> result = querydslRepository.findNameContainingBookPage(pageable,title,0);

            PageableUtil.pageNumCheck(result, pageable);

            return result;
        }catch (InvalidDataAccessApiUsageException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }


    public Page<BookProductGetResponseDto> getBookPageFilterByTags(PageRequestDto pageRequestDto, Set<String> tags, Boolean conditionIsAnd) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try{
            Page<BookProductGetResponseDto> result = querydslRepository.findBooksByTagFilter(tags, conditionIsAnd, pageable);

            PageableUtil.pageNumCheck(result, pageable);

            return result;
        }catch (InvalidDataAccessApiUsageException e){
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public Page<BookProductGetResponseDto> getBookPageFilterByCategory(PageRequestDto pageRequestDto, String categoryName) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<BookProductGetResponseDto> result = querydslRepository.findBooksByCategoryFilter(categoryName, pageable);

            PageableUtil.pageNumCheck(result, pageable);

            return result;
        }catch (InvalidDataAccessApiUsageException e){
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public ProductRegisterResponseDto saveBook(BookProductRegisterRequestDto bookProductRegisterRequestDto) {
        log.error("product name : {}", bookProductRegisterRequestDto);
        if (bookRepository.existsByIsbn(bookProductRegisterRequestDto.isbn())){
            throw new DuplicateException(TYPE);
        }else{
            Product product = productRepository.save(
                    Product.builder()
                    .productName(bookProductRegisterRequestDto.productName())
                    .productPriceStandard(bookProductRegisterRequestDto.productPriceStandard())
                    .productPriceSales(bookProductRegisterRequestDto.productPriceSales())
                    .productInventory(bookProductRegisterRequestDto.productInventory())
                    .productThumbnailUrl(bookProductRegisterRequestDto.cover())
                    .productDescription(bookProductRegisterRequestDto.productDescription())
                    .productPackable(bookProductRegisterRequestDto.packable())
                    .build());

            productCheckUtil.checkProduct(product);

            Set<String> categories = bookProductRegisterRequestDto.categories();
            Set<String> tags = bookProductRegisterRequestDto.tags();

            saveProductCategoryRelation(categories, product);
            saveProductTag(tags, product);

            bookRepository.save(
                    Book.builder()
                    .title(bookProductRegisterRequestDto.title())
                    .publisher(bookProductRegisterRequestDto.publisher())
                    .author(bookProductRegisterRequestDto.author())
                    .pubDate(bookProductRegisterRequestDto.pubDate())
                    .isbn(bookProductRegisterRequestDto.isbn())
                    .isbn13(bookProductRegisterRequestDto.isbn13())
                    .product(product)
                    .build());

            return new ProductRegisterResponseDto(product.getProductId(), product.getProductRegisterDate());
        }
    }


    public ProductUpdateResponseDto updateBook(BookProductUpdateRequestDto bookProductUpdateRequestDto) {
        log.info("updated called, {}", bookProductUpdateRequestDto);
        Long bookId = bookProductUpdateRequestDto.bookId();
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()){
            throw new NotFoundIdException(TYPE, bookId);
        }else {
            Book book = bookOptional.get();
            Product product = book.getProduct();
            productCheckUtil.checkProduct(product);

            product.setProductDescription(bookProductUpdateRequestDto.productDescription());
            product.setProductInventory(bookProductUpdateRequestDto.productInventory());
            product.setProductState(bookProductUpdateRequestDto.productState());
            product.setProductPriceSales(bookProductUpdateRequestDto.productPriceSales());
            product.setProductPackable(bookProductUpdateRequestDto.packable());

            Product updateProduct = productRepository.save(product);
            Set<String> categories = bookProductUpdateRequestDto.categories();
            Set<String> tags = bookProductUpdateRequestDto.tags();

            productCategoryRelationService.clearProductCategoryRelationsByProduct(updateProduct);
            productTagService.clearTagsByProduct(updateProduct);

            saveProductCategoryRelation(categories, updateProduct);
            saveProductTag(tags, updateProduct);

            return new ProductUpdateResponseDto(LocalDateTime.now());
        }
    }
}