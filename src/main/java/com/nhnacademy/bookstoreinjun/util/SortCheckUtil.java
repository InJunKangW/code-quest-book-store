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


    /**
     * pageable 이 잘못된 정렬 조건을 가졌을 경우 커스텀 예외 InValidSortNameException 를 던집니다.
     * @param pageable 예외 객체를 만들 때 사용할 pageable 객체
     * @return pageable 의 sort 값들이 이어진 문자열을 message 로 갖는 InValidSortNameException
     */
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


    /**
     * pageable 의 정렬 조건이 해당 엔티티 클래스를 정렬하기 적절한지 판단합니다.
     * 이 메서드로는 정렬 기준을 외래키의 column 으로 둘 시 예외가 발생합니다.
     * 즉, 외래키 속성으로 정렬하지 않을 엔티티에만 사용해야 합니다.
     * 예를 들어, Book 엔티티의 경우 book.product.productRegisterDate (도서에 대응하는 상품의 등록 시간) 같은 정렬 조건을 사용하려면 이 메서드를 사용하면 안됩니다.
     * 또한, @column(name = ...) 어노테이션으로 실제 필드명과 다른 이름을 지정할 경우 column name 으로 지정된 값이 아니라 실제 필드명으로 정렬 요청해야 합니다.
     * 카테고리의 경우에는 productCategoryId, categoryName, parentProductCategory 로 정렬 가능합니다.
     * @param clazz 정렬 조건을 교차 검증할 클래스
     * @param pageable 정렬 조건을 교차 검증할 pageable
     */
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
