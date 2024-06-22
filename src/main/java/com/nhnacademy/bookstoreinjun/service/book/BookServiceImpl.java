package com.nhnacademy.bookstoreinjun.service.book;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.page.BookPageRequestDto;
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
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.BookRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.service.productCategoryRelation.ProductCategoryRelationServiceImpl;
import com.nhnacademy.bookstoreinjun.service.productTag.ProductTagService;
import com.nhnacademy.bookstoreinjun.service.productCategory.ProductCategoryService;
import com.nhnacademy.bookstoreinjun.service.tag.TagService;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final ProductRepository productRepository;

    private final ProductCategoryService productCategoryService;

    private final ProductCategoryRelationServiceImpl productCategoryRelationServiceImpl;

    private final TagService tagService;

    private final ProductTagService productTagService;


    private final String TYPE = "book";


    private void saveProductCategoryRelation(List<String> categories, Product product){
        if (categories != null){
            for (String categoryName : categories) {
                ProductCategory productCategory = productCategoryService.getCategoryByName(categoryName);

                ProductCategoryRelation productCategoryRelation = ProductCategoryRelation.builder()
                        .productCategory(productCategory)
                        .product(product)
                        .build();

                productCategoryRelationServiceImpl.saveProductCategory(productCategoryRelation);
            }
        }
    }

    private void saveProductTag(List<String> tags, Product product){
        if (tags != null){
            for (String tagName : tags) {
                Tag tag = tagService.getTagByTagName(tagName);

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
                .title(book.getTitle())
                .publisher(book.getPublisher())
                .author(book.getAuthor())
                .pubDate(book.getPubDate())
                .isbn(book.getIsbn())
                .isbn13(book.getIsbn13())
                .cover(book.getProduct().getProductThumbnailUrl())
                .packable(book.getProduct().isPackable())
                .productDescription(book.getProduct().getProductDescription())
                .productRegisterDate(book.getProduct().getProductRegisterDate())
                .productState(book.getProduct().getProductState())
                .productViewCount(book.getProduct().getProductViewCount())
                .productPriceStandard(book.getProduct().getProductPriceStandard())
                .productPriceSales(book.getProduct().getProductPriceSales())
                .productInventory(book.getProduct().getProductInventory())
                .categories(productCategoryRelationServiceImpl.getCategoriesByProduct(book.getProduct()).stream()
                        .map(ProductCategory ::getCategoryName)
                        .collect(Collectors.toList()))
                .tags(productTagService.getTagsByProduct(book.getProduct()).stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toList()))
                .build();
    }

    public Page<BookProductGetResponseDto> getBookPage(@Valid BookPageRequestDto bookPageRequestDto) {
        int page = Objects.requireNonNullElse(bookPageRequestDto.page(),1);
        int size = Objects.requireNonNullElse(bookPageRequestDto.size(),5);
        boolean desc = Objects.requireNonNullElse(bookPageRequestDto.desc(), true);
        String sort =  Objects.requireNonNullElse(bookPageRequestDto.sort(), "product.productRegisterDate");


        Pageable pageable = PageRequest.of(page -1, size,
                Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, sort));

        try{
            Page<Book> bookPage = bookRepository.findBooksByProductState(pageable);
            int total = bookPage.getTotalPages();

            if (total < page){
                throw new PageOutOfRangeException(total, page);
            }

            return bookPage.map(this::makeBookProductGetResponseDtoFromBook);

        }catch (InvalidDataAccessApiUsageException e) {
            StringBuilder stringBuilder = new StringBuilder();
            Sort sorts = pageable.getSort();
            List<Sort.Order> orders = sorts.toList();

            for (Sort.Order order : orders) {
                stringBuilder.append(order.getProperty());
                if (order != orders.getLast()){
                    stringBuilder.append(", ");
                }
            }
            throw new InvalidSortNameException(stringBuilder.toString());
        }
    }


    public ProductRegisterResponseDto saveBook(BookProductRegisterRequestDto bookProductRegisterRequestDto) {
        if (bookRepository.existsByIsbn13(bookProductRegisterRequestDto.isbn13())){
            throw new DuplicateException(TYPE);
        }else{
            Product product = productRepository.save(
                    Product.builder()
                    .productName(bookProductRegisterRequestDto.title())
                    .productPriceStandard(bookProductRegisterRequestDto.productPriceStandard())
                    .productPriceSales(bookProductRegisterRequestDto.productPriceSales())
                    .productInventory(bookProductRegisterRequestDto.productInventory())
                    .productThumbnailUrl(bookProductRegisterRequestDto.cover())
                    .productDescription(bookProductRegisterRequestDto.productDescription())
                    .packable(bookProductRegisterRequestDto.packable())
                    .build());

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


    public BookProductGetResponseDto getBookByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null){
            throw new NotFoundIdException(TYPE, bookId);
        }else{
            return makeBookProductGetResponseDtoFromBook(book);
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
            ProductCheckUtil.checkProduct(product);

            product.setProductDescription(bookProductUpdateRequestDto.productDescription());
            product.setProductInventory(bookProductUpdateRequestDto.productInventory());
            product.setProductState(bookProductUpdateRequestDto.productState());
            product.setProductPriceSales(bookProductUpdateRequestDto.productPriceSales());

            Product updateProduct = productRepository.save(product);
            List<String> categories = bookProductUpdateRequestDto.categories();
            List<String> tags = bookProductUpdateRequestDto.tags();

            productCategoryRelationServiceImpl.clearCategoriesByProduct(updateProduct);
            productTagService.clearTagsByProduct(updateProduct);

            saveProductCategoryRelation(categories, updateProduct);
            saveProductTag(tags, updateProduct);

            return new ProductUpdateResponseDto(LocalDateTime.now());
        }
    }
}
