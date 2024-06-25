package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    Page<Product> findByProductNameContaining(Pageable pageable, String productName);

    @Query("SELECT DISTINCT p FROM Product p " +
            "WHERE EXISTS (" +
            "  SELECT pt FROM ProductTag pt JOIN pt.tag t " +
            "  WHERE pt.product = p AND t.tagName IN :tags" +
            ") " +
            "GROUP BY p " +
            "HAVING COUNT(DISTINCT p.productId) = :tagCount")
    List<Product> findByTags(@Param("tags") List<String> tags, @Param("tagCount") Long tagCount);


//    public List<Product> findByProductNameLike(String productName);
//
//    public List<Product> findByProductPriceSalesBetween(long minPrice, long maxPrice);
//
//    public List<Product> findByProductPriceSalesAfter(long minPrice);
//
//    public List<Product> findByProductPriceSalesBefore(long maxPrice);
//
//    public List<Product> findByProductPriceSalesLessThan(long minPrice);
//
//    public List<Product> findByProductPriceSalesGreaterThan(long maxPrice);
//
//    public List<Product> findByProductNameContaining(String productName);
//
//    public List<Product> findByProductNameStartingWith(String productName);
//
//    public List<Product> findByProductIdOrderByProductViewCount(Long productId);
}
