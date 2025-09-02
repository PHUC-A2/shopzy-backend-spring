package com.example.shopzy.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.shopzy.domain.entity.Product;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.repository.ProductRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }

    public ResultPaginationDTO getAllProducts(Specification<Product> spec, Pageable pageable) {

        Page<Product> pageProduct = this.productRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageProduct.getTotalPages());
        mt.setTotal(pageProduct.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageProduct.getContent());

        return rs;
    }

    public Product getProductById(Long id) throws IdInvalidException {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new IdInvalidException("Không tìm thấy Product với ID = " + id);
        }
    }

    public Product updateProduct(Product productReq) throws IdInvalidException {
        Product product = this.getProductById(productReq.getId());

        if (product == null) {
            throw new IdInvalidException("Không tìm thấy Product với ID = " + productReq.getId());
        }

        product.setName(productReq.getName());
        product.setDescription(productReq.getDescription());
        product.setPrice(productReq.getPrice());
        product.setStock(productReq.getStock());
        // không cần update status vì tự động cập nhật dựa vào stock
        product.setProductCondition(productReq.getProductCondition());
        product.setImageUrl(productReq.getImageUrl());
        product.setSize(productReq.getSize());
        product.setColor(productReq.getColor());

        return this.productRepository.save(product);
    }

    public void deleteProduct(Long id) throws IdInvalidException {

        Product product = this.getProductById(id);
        if (product == null) {
            throw new IdInvalidException("Không tìm thấy Product với ID = " + id);
        }
        this.productRepository.deleteById(id);
    }

}
