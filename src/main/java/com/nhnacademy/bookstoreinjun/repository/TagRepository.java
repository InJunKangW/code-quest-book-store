package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
