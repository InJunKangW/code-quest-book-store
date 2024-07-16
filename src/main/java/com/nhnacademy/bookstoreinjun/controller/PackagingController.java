package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.service.packaging.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class PackagingController {

    private final PackagingService packagingService;

    @GetMapping("/packaging/single/byPackage/{packageId}")
    public ResponseEntity<PackagingGetResponseDto> getPackageInfoByPackageID(@PathVariable("packageId") Long packageId) {
        PackagingGetResponseDto info = packagingService.getPackageInfoById(packageId);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping("/packaging/name")
    public ResponseEntity<PackagingGetResponseDto> getPackageInfoByName(@RequestParam() String packageName) {
        PackagingGetResponseDto info = packagingService.getPackageInfoByPackageName(packageName);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping("/packaging/single/byProduct/{productId}")
    public ResponseEntity<PackagingGetResponseDto> getPackageInfoByProductId(@PathVariable("productId") Long productId) {
        PackagingGetResponseDto info = packagingService.getPackageInfoByProductId(productId);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @PostMapping("/admin/packaging/register")
    public ResponseEntity<ProductRegisterResponseDto> insertPackage(@RequestBody PackagingRegisterRequestDto req) {
        return ResponseEntity.ok(packagingService.registerPackage(req));
    }

    @PutMapping("/admin/packaging/update")
    public ResponseEntity<ProductUpdateResponseDto> updatePackage(@RequestBody PackagingUpdateRequestDto req) {
        return new ResponseEntity<>(packagingService.updatePackageInfo(req), HttpStatus.OK);
    }

    @GetMapping("/packaging/all")
    public ResponseEntity<List<PackagingGetResponseDto>> getAllPackage(
            @RequestParam(name = "productState", required = false) Integer productState
    ) {
        return new ResponseEntity<>(packagingService.getAllPackages(productState), HttpStatus.OK);
    }

    @GetMapping("/packaging/page")
    public ResponseEntity<Page<PackagingGetResponseDto>> getAllPackageByProductId(
            @RequestParam(name = "productState", required = false) Integer productState,
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return new ResponseEntity<>(packagingService.getPackagesPage(productState, page, size), HttpStatus.OK);
    }
}
