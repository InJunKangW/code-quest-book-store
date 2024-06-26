package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SortCheckUtil {

    public SortCheckUtil() {
        throw new RuntimeException("utility class");
    }

    public static InvalidSortNameException ThrowInvalidSortNameException(Pageable pageable){
        StringBuilder stringBuilder = new StringBuilder();
        Sort sorts = pageable.getSort();
        List<Sort.Order> orders = sorts.toList();

        for (Sort.Order order : orders) {
            stringBuilder.append(order.getProperty());
            if (order != orders.getLast()){
                stringBuilder.append(", ");
            }
        }
        return new InvalidSortNameException(stringBuilder.toString());
    }
}
