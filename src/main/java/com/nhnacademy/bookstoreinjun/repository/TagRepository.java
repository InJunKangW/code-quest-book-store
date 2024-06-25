package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * 태그명에 해당하는 데이터가 존재하는지 확인합니다. 태그명을 Unique 로 지정했기 때문에 중복 여부를 확인하기 위해 사용합니다.
     * @param tagName 중복을 확인할 태그명
     * @return 해당 태명이 이미 데이터베이스에 존재하는 지 여부
     */
    boolean existsByTagName(String tagName);

    /**
     * 태그명에 해당하는 태그를 찾아냅니다. 태그명을 Unique 로 지정했기 때문에 단일 데이터가 반환됩니다.
     * @param tagName 찾아낼 카테고리명
     * @return 해당 태그명을 가지는 태그
     */
    Tag findByTagName(String tagName);

    /**
     * 특정 태그명을 포함하는 모든 태그 리스트를 반환합니다.
     * @param tagName 포함 여부를 확인할 태그명
     * @return 해당 태그명을 포함하는 모든 태그의 리스트
     */
    List<Tag> findAllByTagNameContaining(String tagName);

    Page<Tag> findAllByTagNameContaining(Pageable pageable, String tagName);
}
