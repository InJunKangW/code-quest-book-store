package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.packaging.PackageInfoResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackageInsertRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackageUpdateRequestDto;
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

    @GetMapping("/single")
    public ResponseEntity<PackageInfoResponseDto> getPackageInfoByPackageID(@RequestParam Long packageId) {
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

    @GetMapping("/asd")
    public ResponseEntity<PackageInfoResponseDto> getPackageInfoByProductId(@RequestParam Long productId) {
        PackageInfoResponseDto info = packagingServiceImpl.getPackageInfoByProductId(productId);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updatePackage(@RequestBody PackageUpdateRequestDto req) {
        if (packagingServiceImpl.updatePackageInfo(req)) {
            return ResponseEntity.ok(true);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> insertPackage(@RequestBody PackageInsertRequestDto req) {
        if (packagingServiceImpl.registerPackage(req)) {
            return ResponseEntity.ok(true);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<PackageInfoResponseDto>> getAllPackage() {
        return new ResponseEntity<>(packagingServiceImpl.getAllPackages(), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<PackageInfoResponseDto>> getAllPackageByProductId(@RequestParam(name = "page") int page, @RequestParam(name = "page") int size) {
        return new ResponseEntity<>(packagingServiceImpl.getPackagesPage(page, size), HttpStatus.OK);
    }
}
