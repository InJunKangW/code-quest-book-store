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
import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.BookQuerydslRepository;
import com.nhnacademy.bookstoreinjun.repository.BookRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRelationRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.service.product.ProductDtoService;
import com.nhnacademy.bookstoreinjun.service.productCategoryRelation.ProductCategoryRelationService;
import com.nhnacademy.bookstoreinjun.service.productTag.ProductTagService;
import com.nhnacademy.bookstoreinjun.util.MakePageableUtil;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final ProductTagRepository productTagRepository;
    private final ProductCategoryRelationRepository productCategoryRelationRepository;

    private final int DEFAULT_PAGE_SIZE = 5;
    private final String DEFAULT_SORT = "product.productRegisterDate";


    private void saveProductCategoryRelation(List<String> categories, Product product){
        for (String categoryName : categories) {
            ProductCategory productCategory = productCategoryRepository.findByCategoryName(categoryName);
            if (productCategory == null){
                throw new NotFoundNameException(TYPE, categoryName);
            }

            ProductCategoryRelation productCategoryRelation = ProductCategoryRelation.builder()
                    .productCategory(productCategory)
                    .product(product)
                    .build();

            productCategoryRelationService.saveProductCategoryRelation(productCategoryRelation);
        }
    }

    private void saveProductTag(List<String> tags, Product product){
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

    private BookProductGetResponseDto makeBookProductGetResponseDtoFromBook(Book book){
        return BookProductGetResponseDto.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .publisher(book.getPublisher())
                .author(book.getAuthor())
                .pubDate(book.getPubDate())
                .isbn(book.getIsbn())
                .isbn13(book.getIsbn13())
                .productId(book.getProduct().getProductId())
                .cover(book.getProduct().getProductThumbnailUrl())
                .productName(book.getProduct().getProductName())
                .packable(book.getProduct().isProductPackable())
                .productDescription(book.getProduct().getProductDescription())
                .productRegisterDate(book.getProduct().getProductRegisterDate())
                .productState(book.getProduct().getProductState())
                .productViewCount(book.getProduct().getProductViewCount())
                .productPriceStandard(book.getProduct().getProductPriceStandard())
                .productPriceSales(book.getProduct().getProductPriceSales())
                .productInventory(book.getProduct().getProductInventory())
                .categories(querydslRepository.getAllProductCategoryName(book.getProduct()))
                .tags(querydslRepository.getAllTagName(book.getProduct()))
                .build();
    }


    private Page<BookProductGetResponseDto> makeValidBookPage(Page<Book> bookPage, int requestPage){
        int total = bookPage.getTotalPages();

        if (total != 0 && total < requestPage){
            throw new PageOutOfRangeException(total, requestPage);
        }

        return bookPage.map(this::makeBookProductGetResponseDtoFromBook);
    }

    public BookProductGetResponseDto getBookByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null){
            throw new NotFoundIdException(TYPE, bookId);
        }else{
            Product product = book.getProduct();
            productCheckUtil.checkProduct(product);
            productRepository.addProductViewCount(product.getProductId());
            return makeBookProductGetResponseDtoFromBook(book);
        }
    }

    public Page<BookProductGetResponseDto> getBookPage(PageRequestDto pageRequestDto) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        int requestPage = pageable.getPageNumber() + 1;
        try{
            Page<Book> bookPage = bookRepository.findBooksByProductState(pageable);
            return makeValidBookPage(bookPage, requestPage);
        }catch (InvalidDataAccessApiUsageException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public Page<BookProductGetResponseDto> getNameContainingBookPage(PageRequestDto pageRequestDto, String title) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        int requestPage = pageable.getPageNumber() + 1;
        try{
            Page<Book> bookPage = bookRepository.findBooksByProductStateAndNameContaining(pageable, title);
            return makeValidBookPage(bookPage, requestPage);
        }catch (InvalidDataAccessApiUsageException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }


    public Page<BookProductGetResponseDto> getBookPageFilterByTags(PageRequestDto pageRequestDto, Set<String> tags, Boolean conditionIsAnd) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        int requestPage = pageable.getPageNumber() + 1;

        try{
            Page<BookProductGetResponseDto> bookPageFilterByTags = querydslRepository.findBooksByTagFilter(tags, conditionIsAnd, pageable);
            int total = bookPageFilterByTags.getTotalPages();
            if (total != 0 && total < requestPage){
                throw new PageOutOfRangeException(total, requestPage);
            }
            return bookPageFilterByTags;
        }catch (InvalidDataAccessApiUsageException e){
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public Page<BookProductGetResponseDto> getBookPageFilterByCategories(PageRequestDto pageRequestDto, Set<String> categories, Boolean conditionIsAnd) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        int requestPage = pageable.getPageNumber() + 1;

        try {
            Page<BookProductGetResponseDto> result = querydslRepository.findBooksByCategoryFilter(categories, conditionIsAnd, pageable);
            int total = result.getTotalPages();

            if (total != 0 && total < requestPage){
                throw new PageOutOfRangeException(total, requestPage);
            }
            return result;
        }catch (InvalidDataAccessApiUsageException e){
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public ProductRegisterResponseDto saveBook(BookProductRegisterRequestDto bookProductRegisterRequestDto) {
        log.error("product name : {}", bookProductRegisterRequestDto.productName());
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

            List<String> categories = bookProductRegisterRequestDto.categories();
            List<String> tags = bookProductRegisterRequestDto.tags();

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
            List<String> categories = bookProductUpdateRequestDto.categories();
            List<String> tags = bookProductUpdateRequestDto.tags();

            productCategoryRelationService.clearProductCategoryRelationsByProduct(updateProduct);
            productTagService.clearTagsByProduct(updateProduct);

            saveProductCategoryRelation(categories, updateProduct);
            saveProductTag(tags, updateProduct);

            return new ProductUpdateResponseDto(LocalDateTime.now());
        }
    }
}
