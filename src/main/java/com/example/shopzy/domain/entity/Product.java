package com.example.shopzy.domain.entity;

import java.time.Instant;

import com.example.shopzy.util.SecurityUtil;
import com.example.shopzy.util.constant.product.ProductConditionEnum;
import com.example.shopzy.util.constant.product.ProductStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * +) Sản Phẩm
 * id : Mã sản phẩm
 * name : Tên sản phẩm
 * description : Mô tả sản phẩm
 * price : Giá bán
 * stock : Số lượng tồn kho
 * status : Trạng thái hàng (IN_STOCK = còn hàng, OUT_OF_STOCK = hết hàng)
 * condition : Tình trạng hàng (NEW = hàng mới, USED = hàng cũ/second-hand)
 * imageUrl : Ảnh sản phẩm
 * size : Kích thước (S, M, L, XL)
 * color : Màu sắc
 * createdAt, createdBy, updatedAt, updatedBy
 * 
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private double price;
    private int stock; // Số lượng tồn kho
    // mặc định nếu số lượng tồn khi lớn hơn 1 sẽ còn hàng ngược lại hết hàng
    @Enumerated(EnumType.STRING)
    private ProductStatusEnum status; // IN_STOCK / OUT_OF_STOCK

    // nếu để condition sẽ gây lỗi vì trùng từ khóa của MySQL
    @Enumerated(EnumType.STRING)
    private ProductConditionEnum productCondition = ProductConditionEnum.NEW; // mặc định là tình trạng hàng mới

    @Column(columnDefinition = "MEDIUMTEXT")
    private String imageUrl;
    private String size;
    private String color;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    // dùng để cập nhật người tạo ra người dùng
    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.createdAt = Instant.now(); // tạo ra lúc
        // tạo status dựa vào stock
        this.status = this.stock > 0 ? ProductStatusEnum.IN_STOCK : ProductStatusEnum.OUT_OF_STOCK;
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.updatedAt = Instant.now(); // tạo ra lúc

        // update status dựa vào stock
        this.status = this.stock > 0 ? ProductStatusEnum.IN_STOCK : ProductStatusEnum.OUT_OF_STOCK;
    }

    // setter cập nhật cho stock tránh lỗi khi chưa khởi tạo giá trị stock mà lại
    // gắn cho status
    // dùng setter cho stock để tự động cập nhật status khi stock thay đổi
    public void setStock(int stock) {
        this.stock = stock;
        this.status = this.stock > 0 ? ProductStatusEnum.IN_STOCK : ProductStatusEnum.OUT_OF_STOCK;
    }

    public Product(Long id, String name, String description, double price, int stock,
            ProductConditionEnum productCondition, String imageUrl, String size, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.setStock(stock); // gắn trực tiếp cho stock để tự động cập nhật cho status
        this.productCondition = productCondition;
        this.imageUrl = imageUrl;
        this.size = size;
        this.color = color;
    }

}
