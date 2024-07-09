//package com.nhnacademy.bookstoreinjun.book.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
//import com.nhnacademy.bookstoreinjun.dto.book.BookProductUpdateRequestDto;
//import com.nhnacademy.bookstoreinjun.entity.Book;
//import com.nhnacademy.bookstoreinjun.entity.Product;
//import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
//import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
//import com.nhnacademy.bookstoreinjun.entity.ProductTag;
//import com.nhnacademy.bookstoreinjun.entity.Tag;
//import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
//import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
//import com.nhnacademy.bookstoreinjun.repository.QuerydslRepository;
//import com.nhnacademy.bookstoreinjun.repository.BookRepository;
//import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
//import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
//import com.nhnacademy.bookstoreinjun.repository.TagRepository;
//import com.nhnacademy.bookstoreinjun.service.book.BookServiceImpl;
//import com.nhnacademy.bookstoreinjun.service.productCategoryRelation.ProductCategoryRelationService;
//import com.nhnacademy.bookstoreinjun.service.productTag.ProductTagService;
//import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
//import java.time.LocalDate;
//import java.util.Optional;
//import java.util.Set;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class BookServiceTest {
//
//    @InjectMocks
//    private BookServiceImpl bookService;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private TagRepository tagRepository;
//
//    @Mock
//    private ProductCategoryRepository productCategoryRepository;
//
//    @Mock
//    private ProductCategoryRelationService productCategoryRelationService;
//
//    @Mock
//    private ProductTagService productTagService;
//
//    @Mock
//    private QuerydslRepository querydslRepository;
//
//    @Mock
//    private ProductCheckUtil productCheckUtil;
//
//
//    @Test
//    public void saveTest(){
//        when(bookRepository.save(any(Book.class))).thenReturn(new Book());
//        when(productRepository.save(any(Product.class))).thenReturn(new Product());
//        when(productCategoryRepository.findByCategoryName(any())).thenReturn(new ProductCategory());
//        when(tagRepository.findByTagName(any())).thenReturn(new Tag());
//        BookProductRegisterRequestDto dto = BookProductRegisterRequestDto.builder()
//                .title("test title")
//                .pubDate(LocalDate.now())
//                .publisher("test publisher")
//                .author("test author")
//                .cover("test cover")
//                .isbn("123456789a")
//                .isbn13("123456789abcd")
//                .productName("T")
//                .productDescription("test product description")
//                .productInventory(0)
//                .productPriceStandard(1)
//                .productPriceSales(1)
//                .packable(false)
//                .categories(Set.of("test category1","test category2"))
//                .tags(Set.of("test tag1","test tag2"))
//                .build();
//
//        bookService.saveBook(dto);
//
//        verify(bookRepository,times(1)).save(any(Book.class));
//        verify(productRepository,times(1)).save(any(Product.class));
//        verify(tagRepository,times(2)).findByTagName(any(String.class));
//        verify(productCategoryRepository,times(2)).findByCategoryName(any(String.class));
//        verify(productTagService,times(2)).saveProductTag(any(ProductTag.class));
//        verify(productCategoryRelationService,times(2)).saveProductCategoryRelation(any(ProductCategoryRelation.class));
//    }
//
//    @Test
//    public void updateTestSuccess(){
//        Book testBook = new Book();
//        Product testProduct = new Product();
//        testBook.setProduct(testProduct);
//
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
//        when(productCategoryRepository.findByCategoryName("test category1")).thenReturn(new ProductCategory());
//        when(productCategoryRepository.findByCategoryName("test category2")).thenReturn(new ProductCategory());
//
//        when(tagRepository.findByTagName("test tag1")).thenReturn(new Tag());
//        when(tagRepository.findByTagName("test tag2")).thenReturn(new Tag());
//
//        BookProductUpdateRequestDto dto = BookProductUpdateRequestDto.builder()
//                .bookId(1L)
//                .productName("update product name")
//                .productDescription("update product description")
//                .packable(true)
//                .productPriceSales(123)
//                .productInventory(1234)
//                .productState(1)
//                .categories(Set.of("test category1","test category2"))
//                .tags(Set.of("test tag1","test tag2"))
//                .build();
//
//        bookService.updateBook(dto);
//        verify(bookRepository,times(1)).findById(1L);
//        verify(productCheckUtil,times(1)).checkProduct(any());
//        verify(productRepository,times(1)).save(any(Product.class));
//        verify(productCategoryRelationService,times(1)).clearProductCategoryRelationsByProduct(any());
//        verify(productTagService,times(1)).clearTagsByProduct(any());
//        verify(productCategoryRelationService,times(2)).saveProductCategoryRelation(any());
//        verify(productTagService,times(2)).saveProductTag(any());
//    }
//
//    @Test
//    public void updateTestFailByNotExistingBook(){
//        BookProductUpdateRequestDto dto = BookProductUpdateRequestDto.builder()
//                .bookId(1L)
//                .productName("update product name")
//                .productDescription("update product description")
//                .packable(true)
//                .productPriceSales(123)
//                .productInventory(1234)
//                .productState(1)
//                .categories(Set.of("test category1", "test category2"))
//                .tags(Set.of("test tag1"))
//                .build();
//
//        assertThrows(NotFoundIdException.class, () -> bookService.updateBook(dto));
//    }
//
//
//    @Test
//    public void updateTestFailureByNotExistingTag() {
//        Book testBook = new Book();
//        Product testProduct = new Product();
//        testBook.setProduct(testProduct);
//
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
//        when(productCategoryRepository.findByCategoryName("test category1")).thenReturn(new ProductCategory());
//        when(productCategoryRepository.findByCategoryName("test category2")).thenReturn(new ProductCategory());
//
//        when(tagRepository.findByTagName("test tag1")).thenReturn(null);
//
//        BookProductUpdateRequestDto dto = BookProductUpdateRequestDto.builder()
//                .bookId(1L)
//                .productName("update product name")
//                .productDescription("update product description")
//                .packable(true)
//                .productPriceSales(123)
//                .productInventory(1234)
//                .productState(1)
//                .categories(Set.of("test category1", "test category2"))
//                .tags(Set.of("test tag1"))
//                .build();
//
//        assertThrows(NotFoundNameException.class, () -> bookService.updateBook(dto));
//    }
//
//    @Test
//    public void updateTestFailureByNotExistingProductCategory() {
//        Book testBook = new Book();
//        Product testProduct = new Product();
//        testBook.setProduct(testProduct);
//
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
//        when(productCategoryRepository.findByCategoryName("test category1")).thenReturn(null);
//
//        BookProductUpdateRequestDto dto = BookProductUpdateRequestDto.builder()
//                .bookId(1L)
//                .productName("update product name")
//                .productDescription("update product description")
//                .packable(true)
//                .productPriceSales(123)
//                .productInventory(1234)
//                .productState(1)
//                .categories(Set.of("test category1"))
//                .build();
//
//        assertThrows(NotFoundNameException.class, () -> bookService.updateBook(dto));
//    }
//
////    @Test
////    public void getIndividualBookTestSuccess(){
////        Book testBook = new Book();
////        testBook.setBookId(1L);
////        Product testProduct = new Product();
////        testProduct.setProductId(3L);
////        testProduct.setProductCategoryRelations(new ArrayList<>());
////        testProduct.setProductTags(new ArrayList<>());
////
////        testBook.setProduct(testProduct);
////
////        when(bookRepository.existsById(1L)).thenReturn(true);
////
////        when(querydslRepository.findBookByBookId(1L)).thenReturn(BookProductGetResponseDto.builder()
////                .categories(Set.of("test category1", "test category2"))
////                .tags(Set.of("test tag1", "test tag2", "test tag3"))
////                .build());
////        BookProductGetResponseDto dto = bookService.getBookByBookId(1L);
////
////        verify(bookRepository,times(1)).existsById(1L);
////        assertNotNull(dto);
////        assertEquals(2, dto.categories().size());
////        assertEquals(3, dto.tags().size());
////    }
//
//    @Test
//    public void getIndividualBookTestFailure() {
//        assertThrows(NotFoundIdException.class, () -> bookService.getBookByProductId(1L, 1L));
//    }
//
////    @Test
////    public void getBookPageTestSuccess(){
//////        Book testBook = new Book();
//////        testBook.setBookId(1L);
//////        Product testProduct = new Product();
//////        testProduct.setProductId(3L);
//////        testProduct.setProductCategoryRelations(new ArrayList<>());
//////        testProduct.setProductTags(new ArrayList<>());
//////
//////        List<ProductCategoryRelation> testProductCategories = Arrays.asList(
//////                ProductCategoryRelation.builder()
//////                        .product(testProduct)
//////                        .productCategory(
//////                                ProductCategory.builder()
//////                                        .categoryName("test category1")
//////                                        .build()
//////                        ).build(),
//////                ProductCategoryRelation.builder()
//////                        .product(testProduct)
//////                        .productCategory(
//////                                ProductCategory.builder()
//////                                        .categoryName("test category2")
//////                                        .build()
//////                        ).build()
//////        );
//////
//////        testProduct.setProductCategoryRelations(testProductCategories);
//////
//////        List<ProductTag> testProductTags = Arrays.asList(
//////                ProductTag.builder()
//////                        .product(testProduct)
//////                        .tag(
//////                                Tag.builder()
//////                                        .tagName("test tag1")
//////                                        .build()
//////                        ).build(),
//////                ProductTag.builder()
//////                        .product(testProduct)
//////                        .tag(
//////                                Tag.builder()
//////                                        .tagName("test tag2")
//////                                        .build()
//////                        ).build(),
//////                ProductTag.builder()
//////                        .product(testProduct)
//////                        .tag(
//////                                Tag.builder()
//////                                        .tagName("test tag3")
//////                                        .build()
//////                        ).build()
//////        );
//////        testProduct.setProductTags(testProductTags);
//////
//////        testBook.setProduct(testProduct);
////
////        PageRequestDto dto = PageRequestDto.builder().build();
////        Pageable pageable = PageableUtil.makePageable(dto, 5, "product.productRegisterDate");
////
////        when(querydslRepository.findAllBookPage(eq(1L), any(), eq(0))).thenReturn(new PageImpl<>(Collections.emptyList()));
//////        Page<BookProductGetResponseDto> bookPage = bookService.getBookPage(1L, dto);
////
//////        assertNotNull(bookPage);
////        verify(querydslRepository,times(1)).findAllBookPage(eq(1L), any(), eq(0));
//////        assertEquals(2, bookPage.getTotalElements());
////    }
//
////    @Test
////    public void getBookPageTestFailureByOutOfPageRange(){
////        Book testBook = new Book();
////        Product testProduct = new Product();
////        testBook.setProduct(testProduct);
////
////        PageRequestDto dto = PageRequestDto.builder()
////                .page(10)
////                .build();
////
////
////        when(querydslRepository.findAllBookPage(eq(1L),any(), eq(0))).thenReturn(new PageImpl<>(new ArrayList<>()));
//////        assertThrows(PageOutOfRangeException.class, () -> bookService.getBookPage(1L, dto));
////    }
//
////    @Test
////    public void getBookPageTestFailureByWrongSort(){
////        Book testBook = new Book();
////        Product testProduct = new Product();
////        testBook.setProduct(testProduct);
////
////        PageRequestDto dto = PageRequestDto.builder()
////                .sort("wrong sort")
////                .build();
////
////        when(querydslRepository.findAllBookPage(eq(1L), any(), eq(0))).thenThrow(InvalidDataAccessApiUsageException.class);
//////        assertThrows(InvalidSortNameException.class, () -> bookService.getBookPage(1L, dto));
////    }
//}
