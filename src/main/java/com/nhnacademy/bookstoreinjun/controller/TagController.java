package com.nhnacademy.bookstoreinjun.controller;


import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.service.tag.TagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<TagGetResponseDto> getTag(@RequestParam("tagName") String tagName) {
        return ResponseEntity.ok(tagService.getTagDtoByTagName(tagName));
    }

    @GetMapping("/list")
    public ResponseEntity<List<TagGetResponseDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/list/containing")
    public ResponseEntity<List<TagGetResponseDto>> getTagByTagName(@RequestParam("tagName") String tagName) {
            return ResponseEntity.ok(tagService.getTagsContaining(tagName));
    }

    @PostMapping
    public ResponseEntity<TagRegisterResponseDto> createTag(@RequestBody TagRegisterRequestDto tagRegisterRequestDto) {
        log.info("post tag called");
        return new ResponseEntity<>(tagService.saveTag(tagRegisterRequestDto), HttpStatus.CREATED);
    }
}
