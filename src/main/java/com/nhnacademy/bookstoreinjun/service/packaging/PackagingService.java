package com.nhnacademy.bookstoreinjun.service.packaging;

import com.nhnacademy.bookstoreinjun.dto.packaging.PackageInfoResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackageInsertRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackageUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PackagingService {
    PackageInfoResponseDto getPackageInfoById(Long packageId);
    PackageInfoResponseDto getPackageInfoByProductId(Long productId);
    PackageInfoResponseDto getPackageInfoByPackageName(String PackageName);
    boolean updatePackageInfo(PackageUpdateRequestDto packaging);
    boolean registerPackage(PackageInsertRequestDto packaging);
    List<PackageInfoResponseDto> getAllPackages();
    Page<PackageInfoResponseDto> getPackagesPage(int page, int size);
}
