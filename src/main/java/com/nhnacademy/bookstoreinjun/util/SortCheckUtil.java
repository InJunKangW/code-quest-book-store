package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import java.lang.reflect.Field;
import java.util.Arrays;
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


    // 이 메서드로는 정렬 기준을 외래키의 column 으로 둘 시 예외가 발생합니다. 즉, 외래키 속성으로 정렬하지 않을 엔티티에만 사용합니다.
    // 또한, column name 어노테이션으로 실제 필드명과 다른 이름을 지정할 경우 column name 으로 지정된 값이 아니라 실제 필드명으로 정렬 요청해야 합니다.
    // 예를 들어, Book 엔티티의 경우 book.product.productRegisterDate (도서에 대응하는 상품의 등록 시간) 같은 정렬 조건을 사용하려면 이 메서드를 사용하면 안됩니다.
    // 카테고리의 경우에는 productCategoryId, categoryName, parentProductCategory로 정렬 가능합니다.
    public static void pageSortCheck(Class<?> clazz, Pageable pageable){
        List<String> classFields = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .toList();
        Sort sort = pageable.getSort();
        for (Sort.Order order : sort) {
            String fieldName = order.getProperty();
            if (!classFields.contains(fieldName)) {
                throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
            }
        }
    }
}
