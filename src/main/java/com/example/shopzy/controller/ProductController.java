package com.example.shopzy.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopzy.domain.entity.Product;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.service.ProductService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    @ApiMessage("Create a product")
    public ResponseEntity<Product> createProduct(@RequestBody Product productReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.createProduct(productReq));
    }

    @GetMapping("/products")
    @ApiMessage("Get all product")
    public ResponseEntity<ResultPaginationDTO> getAllProducts(
            @Filter Specification<Product> spec, Pageable pageable) {
        return ResponseEntity.ok(this.productService.getAllProducts(spec, pageable));
    }

    @GetMapping("/products/{id}")
    @ApiMessage("Get product by id")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(this.productService.getProductByID(id));
    }

    @PutMapping("/products")
    @ApiMessage("Update a product")
    public ResponseEntity<Product> updateProduct(@RequestBody Product productReq) throws IdInvalidException {
        Product product = this.productService.updateProduct(productReq);
        return ResponseEntity.ok(this.productService.updateProduct(product));
    }

    @DeleteMapping("/products/{id}")
    @ApiMessage("Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) throws IdInvalidException {
        this.productService.deleteProduct(id);
        return ResponseEntity.ok(null);
    }
}
