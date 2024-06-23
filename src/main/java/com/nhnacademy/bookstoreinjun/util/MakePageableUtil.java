package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class MakePageableUtil {

    public MakePageableUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable makePageable(PageRequestDto pageRequestDto, int defaultSize, String defaultSort) {
        int page = Objects.requireNonNullElse(pageRequestDto.page(),1);
        int size = Objects.requireNonNullElse(pageRequestDto.size(),defaultSize);
        boolean desc = Objects.requireNonNullElse(pageRequestDto.desc(), true);
        String sort =  Objects.requireNonNullElse(pageRequestDto.sort(), defaultSort);

        return PageRequest.of(page -1, size, Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, sort));
    }
}
