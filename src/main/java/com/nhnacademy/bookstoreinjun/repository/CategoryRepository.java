package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategoryName(String categoryName);

    Category findByCategoryName(String categoryName);

    List<Category> findAllByCategoryNameContaining(String categoryName);

//    @Query("select Category c from Category c1 join Category c2 on c1 where parentCategory")
//    List<Category> findAllSubCategoriesByCategoryName(String categoryName);

    @Query("SELECT c FROM Category c WHERE c.parentCategory = :parent")
    List<Category> findSubCategoriesByParent(Category parent);
}
