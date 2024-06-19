package com.nhnacademy.bookstoreinjun.controller;


import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.service.TagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;


    @GetMapping("/list")
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }



    @GetMapping("/list/containing")
    public ResponseEntity<List<Tag>> getTagByTagName(@RequestParam("tagName") String tagName) {
        List<Tag> tagList = tagService.getTagsContaining(tagName);
        if (tagList == null || tagList.isEmpty()) {
            throw new NotFoundNameException("tag", tagName);
        }else{
            return ResponseEntity.ok(tagList);
        }
    }

    @PostMapping
    public ResponseEntity<TagRegisterResponseDto> createTag(@RequestBody TagRegisterRequestDto tagRegisterRequestDto) {
        Tag tag = tagService.createTag(tagRegisterRequestDto);
        TagRegisterResponseDto dto = new TagRegisterResponseDto(tag.getTagId(), tag.getTagName());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
