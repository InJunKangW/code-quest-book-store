package com.nhnacademy.bookstoreinjun.service.packaging;

import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingUpdateRequestDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PackagingServiceImpl implements PackagingService {
    private final PackagingRepository packageRepository;
    private final ProductRepository productRepository;
    private final ProductCheckUtil productCheckUtil;

    private PackagingGetResponseDto makeDtoFromPackaging(Packaging packaging) {
        return new PackagingGetResponseDto(
                packaging.getPackageId(),
                packaging.getPackageName(),
                packaging.getProduct().getProductThumbnailUrl(),
                packaging.getProduct().getProductId(),
                packaging.getProduct().getProductName(),
                packaging.getProduct().getProductDescription(),
                packaging.getProduct().getProductState(),
                packaging.getProduct().getProductPriceStandard(),
                packaging.getProduct().getProductPriceSales(),
                packaging.getProduct().getProductInventory());
    }

    public PackagingGetResponseDto getPackageInfoById(Long packageId){
        Packaging packaging = packageRepository.findById(packageId).orElse(null);
        if (packaging == null) {
            return null;
        }
        return makeDtoFromPackaging(packaging);
    }

    public PackagingGetResponseDto getPackageInfoByProductId(Long productId) {
        Packaging packaging = packageRepository.findByProduct_ProductId(productId).orElse(null);
        if (packaging == null) {
            return null;
        }
        return makeDtoFromPackaging(packaging);
    }

    public PackagingGetResponseDto getPackageInfoByPackageName(String packageName) {
        Packaging packaging = packageRepository.findByPackageName(packageName).orElse(null);
        if (packaging == null) {
            return null;
        }
        return makeDtoFromPackaging(packaging);
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
                .packageName(packaging.getPackagingName())
                .build());

        return new ProductRegisterResponseDto(product.getProductId(), product.getProductRegisterDate());
    }

    public ProductUpdateResponseDto updatePackageInfo(PackagingUpdateRequestDto request) {
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
        packaging.setPackageName(request.getPackagingName());

        packageRepository.save(packaging);

        return new ProductUpdateResponseDto(LocalDateTime.now());
    }


    public List<PackagingGetResponseDto> getAllPackages(Integer productState) {
        List<Packaging> packagingList = productState == null ? packageRepository.findAll() : packageRepository.findByProduct_ProductState(productState);
        return packagingList.stream()
                .map(this::makeDtoFromPackaging
                )
                .toList();
    }

    public Page<PackagingGetResponseDto> getPackagesPage(Integer productState, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("productProductId").descending());
        return productState == null ?
                packageRepository.findAll(pageRequest)
                        .map(this::makeDtoFromPackaging) :
                packageRepository.findByProduct_ProductState(productState, pageRequest)
                        .map(this::makeDtoFromPackaging);
    }
}
