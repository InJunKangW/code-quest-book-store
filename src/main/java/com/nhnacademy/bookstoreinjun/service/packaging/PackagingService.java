package com.nhnacademy.bookstoreinjun.service.packaging;

import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PackagingService {
    PackagingGetResponseDto getPackageInfoById(Long packageId);
    PackagingGetResponseDto getPackageInfoByProductId(Long productId);
    PackagingGetResponseDto getPackageInfoByPackageName(String PackageName);
    ProductUpdateResponseDto updatePackageInfo(PackagingUpdateRequestDto packaging);
    ProductRegisterResponseDto registerPackage(PackagingRegisterRequestDto packaging);
    List<PackagingGetResponseDto> getAllPackages(Integer productState);
    Page<PackagingGetResponseDto> getPackagesPage(Integer productState, int page, int size);
}
