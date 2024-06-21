package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<ProductCategory, Long> {

    boolean existsByCategoryName(String categoryName);

    ProductCategory findByCategoryName(String categoryName);

    List<ProductCategory> findAllByCategoryNameContaining(String categoryName);

    @Query("SELECT c FROM ProductCategory c WHERE c.parentProductCategory = :parent")
    List<ProductCategory> findSubCategoriesByParent(ProductCategory parent);
}
