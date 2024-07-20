package com.nhnacademy.bookstoreinjun.tag.entity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nhnacademy.bookstoreinjun.entity.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-dev.properties")
class TagEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void saveTagTest(){
        Tag tag = Tag.builder()
                .tagName("test tag1")
                .build();

        Tag savedTag = entityManager.merge(tag);

        assertNotNull(savedTag);
        assertEquals("test tag1", savedTag.getTagName());
        assertNotNull(savedTag.getTagId());
    }

    @Test
    void updateTagTest(){
        Tag tag = Tag.builder()
                .tagName("test tag1")
                .build();

        Tag savedTag = entityManager.merge(tag);

        savedTag.setTagName("test tag2");

        entityManager.flush();

        Tag updatedTag = entityManager.merge(savedTag);

        assertEquals("test tag2", updatedTag.getTagName());
    }
}
