package com.nhnacademy.bookstoreinjun.tag.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-dev.properties")
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    private Tag tag;

    @BeforeEach
    public void setUp() {
        tag = Tag.builder()
                .tagName("test tag")
                .build();
    }

    @Test
    public void tagSaveTest() {
        Tag savedTag = tagRepository.save(tag);
        assertNotNull(savedTag);
        assertEquals(savedTag.getTagName(), "test tag");
    }

    @Test
    public void tagCheckTest(){
        Tag savedTag = tagRepository.save(tag);
        assertNotNull(savedTag);
        assertTrue(tagRepository.existsByTagName("test tag"));

        Tag foundTag = tagRepository.findByTagName("test tag");
        assertNotNull(foundTag);
        assertEquals(savedTag, foundTag);
    }

    @Test
    public void tagsCheckTest(){
        for (int i = 0; i < 10; i++) {
            String value = "";
            if(i % 2 == 0){
                value = "add";
            }
            Tag tag = Tag.builder()
                    .tagName("test tag" + value + i)
                    .build();
            tagRepository.save(tag);
        }
        List<Tag> tags = tagRepository.findAll();
        assertEquals(10, tags.size());

        List<Tag> tagsNameContaining = tagRepository.findAllByTagNameContaining("add");
        assertEquals(5, tagsNameContaining.size());
    }
}
