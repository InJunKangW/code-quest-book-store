package com.nhnacademy.bookstoreinjun.common;

import com.nhnacademy.bookstoreinjun.feignclient.WebClient;
import com.nhnacademy.bookstoreinjun.service.productCategory.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebApplicationRunner implements ApplicationRunner {
    private final WebClient webClient;
    private final ProductCategoryService productCategoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info(webClient.updateCategory(productCategoryService.getCategoryTree()).getBody());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
