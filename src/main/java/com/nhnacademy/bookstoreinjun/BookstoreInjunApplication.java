package com.nhnacademy.bookstoreinjun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookstoreInjunApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreInjunApplication.class, args);
    }

}
