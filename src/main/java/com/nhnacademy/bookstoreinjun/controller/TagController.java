package com.nhnacademy.bookstoreinjun.controller;


import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.service.TagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/list")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/list/containing")
    public ResponseEntity<Tag> getTagByTagName(@RequestParam("tagName") String tagName) {
        Tag tag = tagService.getTagByTagName(tagName);
        if (tag == null) {
            throw new NotFoundNameException("tag", tagName);
        }else{
            return ResponseEntity.ok(tag);
        }
    }

}
