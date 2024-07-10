package com.nhnacademy.bookstoreinjun.service.packaging;

import com.nhnacademy.bookstoreinjun.dto.packaging.PackageInfoResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackageUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Packaging;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.PackagingRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PackagingServiceImpl implements PackagingService {
    private final PackagingRepository packageRepository;
    private final ProductRepository productRepository;
    private final ProductCheckUtil productCheckUtil;

    public PackageInfoResponseDto getPackageInfoById(Long packageId){
        Packaging packaging = packageRepository.findById(packageId).orElse(null);
        if (packaging == null) {
            return null;
        }
        return new PackageInfoResponseDto(packaging.getPackageId(), packaging.getPackageName(), packaging.getProduct());
    }

    public PackageInfoResponseDto getPackageInfoByProductId(Long productId) {
        Packaging packaging = packageRepository.findByProduct_ProductId(productId).orElse(null);
        if (packaging == null) {
            return null;
        }
        return new PackageInfoResponseDto(packaging.getPackageId(), packaging.getPackageName(), packaging.getProduct());
    }

    public PackageInfoResponseDto getPackageInfoByPackageName(String packageName) {
        Packaging packaging = packageRepository.findByPackageName(packageName).orElse(null);
        if (packaging == null) {
            return null;
        }
        return new PackageInfoResponseDto(packaging.getPackageId(), packaging.getPackageName(), packaging.getProduct());
    }


    public ProductRegisterResponseDto registerPackage(PackagingRegisterRequestDto packaging) {
        Product product = productRepository.save(Product.builder()
                .productName(packaging.getProductName())
                .productInventory(packaging.getProductInventory())
                .productDescription(packaging.getProductDescription())
                .productThumbnailUrl(packaging.getProductThumbnailUrl())
                .productPriceStandard(packaging.getProductPriceStandard())
                .productPriceSales(packaging.getProductPriceSales())
                .build());

        productCheckUtil.checkProduct(product);

        packageRepository.save(Packaging.builder()
                .product(product)
                .packageName(packaging.getPackageName())
                .build());

        return new ProductRegisterResponseDto(product.getProductId(), product.getProductRegisterDate());
    }

    public ProductUpdateResponseDto updatePackageInfo(PackageUpdateRequestDto request) {
        Product product = productRepository.findByProductId(request.getProductId());

        productCheckUtil.checkProduct(product);

        product.setProductName(request.getProductName());
        product.setProductThumbnailUrl(request.getProductThumbnailUrl());
        product.setProductDescription(request.getProductDescription());
        product.setProductPriceStandard(request.getProductPriceStandard());
        product.setProductPriceSales(request.getProductPriceSales());
        product.setProductInventory(request.getProductInventory());
        product.setProductState(request.getProductState());
        productRepository.save(product);

        Packaging packaging = packageRepository.findByProduct_ProductId(request.getProductId()).orElseThrow(() -> new NotFoundIdException("package", request.getProductId()));
        packaging.setPackageName(request.getPackageName());

        packageRepository.save(packaging);

        return new ProductUpdateResponseDto(LocalDateTime.now());
    }


    public List<PackageInfoResponseDto> getAllPackages(Integer productState) {
        List<Packaging> packagingList = productState == null? packageRepository.findAll() : packageRepository.findByProduct_ProductState(productState);
        return packagingList.stream()
                .map(packaging -> new PackageInfoResponseDto(
                        packaging.getPackageId(),
                        packaging.getPackageName(),
                        packaging.getProduct()
                ))
                .toList();
    }

    public Page<PackageInfoResponseDto> getPackagesPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("packageId").descending());
        return packageRepository.findAll(pageRequest).map(packaging -> new PackageInfoResponseDto(
                packaging.getPackageId(),
                packaging.getPackageName(),
                packaging.getProduct()
        ));
    }
}
