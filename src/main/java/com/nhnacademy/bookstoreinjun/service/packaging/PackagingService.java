package com.nhnacademy.bookstoreinjun.service.packaging;

import com.nhnacademy.bookstoreinjun.dto.packaging.PackageInfoResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackageUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PackagingService {
    PackageInfoResponseDto getPackageInfoById(Long packageId);
    PackageInfoResponseDto getPackageInfoByProductId(Long productId);
    PackageInfoResponseDto getPackageInfoByPackageName(String PackageName);
    ProductUpdateResponseDto updatePackageInfo(PackageUpdateRequestDto packaging);
    ProductRegisterResponseDto registerPackage(PackagingRegisterRequestDto packaging);
    List<PackageInfoResponseDto> getAllPackages();
    Page<PackageInfoResponseDto> getPackagesPage(int page, int size);
}
