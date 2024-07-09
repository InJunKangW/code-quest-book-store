package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.packaging.PackageInfoResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackageUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.service.packaging.PackagingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/package")
public class PackageController {

    private final PackagingServiceImpl packagingServiceImpl;

    @GetMapping("/single/byPackage/{packageId}")
    public ResponseEntity<PackageInfoResponseDto> getPackageInfoByPackageID(@PathVariable("packageId") Long packageId) {
        PackageInfoResponseDto info = packagingServiceImpl.getPackageInfoById(packageId);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping("/name")
    public ResponseEntity<PackageInfoResponseDto> getPackageInfoByName(@RequestParam String packageName) {
        PackageInfoResponseDto info = packagingServiceImpl.getPackageInfoByPackageName(packageName);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping("/single/byProduct/{productId}")
    public ResponseEntity<PackageInfoResponseDto> getPackageInfoByProductId(@PathVariable("productId") Long productId) {
        PackageInfoResponseDto info = packagingServiceImpl.getPackageInfoByProductId(productId);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @PostMapping("/register")
    public ResponseEntity<ProductRegisterResponseDto> insertPackage(@RequestBody PackagingRegisterRequestDto req) {
        return ResponseEntity.ok(packagingServiceImpl.registerPackage(req));
    }

    @PutMapping("/update")
    public ResponseEntity<ProductUpdateResponseDto> updatePackage(@RequestBody PackageUpdateRequestDto req) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<PackageInfoResponseDto>> getAllPackage(
            @RequestParam(name = "productState", required = false) Integer productState
    ) {
        return new ResponseEntity<>(packagingServiceImpl.getAllPackages(productState), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<PackageInfoResponseDto>> getAllPackageByProductId(@RequestParam(name = "page") int page, @RequestParam(name = "page") int size) {
        return new ResponseEntity<>(packagingServiceImpl.getPackagesPage(page, size), HttpStatus.OK);
    }
}
