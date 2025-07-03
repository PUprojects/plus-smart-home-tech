package ru.yandex.practicum.commerce.shopping.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.model.Product;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByProductCategory(ProductCategory productCategory, Pageable pageable);
}
