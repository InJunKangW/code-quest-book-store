package com.nhnacademy.bookstoreinjun.service.productTag;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductTagServiceImpl implements ProductTagService {
    private final ProductTagRepository productTagRepository;

    private final TagRepository tagRepository;

    private final ProductCheckUtil productCheckUtil;

    public void saveProductTag(ProductTag productTag) {
        Product product = productTag.getProduct();
        productCheckUtil.checkProduct(product);

        Tag tag = productTag.getTag();
        if (tag == null || tag.getTagId() == null) {
            throw new RuntimeException();
        } else if (!tagRepository.existsById(tag.getTagId())) {
            throw new NotFoundIdException("tag", tag.getTagId());
        }
        productTagRepository.save(productTag);
    }

    public void clearTagsByProduct(Product product) {
        productCheckUtil.checkProduct(product);
        productTagRepository.deleteByProduct(product);
    }
}
