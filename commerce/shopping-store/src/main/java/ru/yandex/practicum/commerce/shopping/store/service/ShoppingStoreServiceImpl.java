package ru.yandex.practicum.commerce.shopping.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductState;
import ru.yandex.practicum.commerce.exception.shopping.store.ProductNotFoundException;
import ru.yandex.practicum.commerce.request.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.shopping.store.model.Product;
import ru.yandex.practicum.commerce.shopping.store.repository.ProductRepository;
import ru.yandex.practicum.commerce.shopping.store.service.mapper.ProductMapper;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDto> getProducts(String category, Pageable pageable) {
        Page<Product> result = productRepository.findByProductCategory(ProductCategory.valueOf(category), pageable);
        log.info("Service result: {}", result);
        return result.map(productMapper::toProductDto);
    }

    @Override
    public ProductDto createNewProduct(ProductDto newProduct) {
        return productMapper.toProductDto(productRepository.save(productMapper.toProduct(newProduct)));
    }

    @Override
    public ProductDto updateProduct(ProductDto updatedProduct) {
        productRepository.findById(updatedProduct.productId())
                .orElseThrow(() -> new ProductNotFoundException("Продукт " + updatedProduct.productId() +
                    " не найден"));

        return productMapper.toProductDto(productRepository.save(productMapper.toProduct(updatedProduct)));
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Продукт " + productId +
                        " не найден"));

        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Optional<Product> optionalProduct = productRepository.findById(request.productId());
        if(optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setQuantityState(request.quantityState());
            productRepository.save(product);
            return true;
        }
        return false;
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return productMapper.toProductDto(
                productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Продукт " + productId +
                        " не найден")));
    }
}
