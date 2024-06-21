package com.nhnacademy.bookstoreinjun.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "aladinClient", url = "https://www.aladin.co.kr")
public interface AladinClient {

    @GetMapping("/ttb/api/ItemSearch.aspx")
    public String getBooks(
            @RequestParam("TTBKey") String ttbKey,
            @RequestParam("Query") String query,
            @RequestParam("QueryType") String queryType,
            @RequestParam("Cover") String Cover,
            @RequestParam("MaxResults") int maxResults
            );
}
