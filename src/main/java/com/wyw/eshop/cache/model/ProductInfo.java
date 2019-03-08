package com.wyw.eshop.cache.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "product_info")
public class ProductInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "pictures")
    private String pictures;

    @Column(name = "specification")
    private String specification;

    @Column(name = "service")
    private String service;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "shop_id")
    private Integer shopId;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return pictures
     */
    public String getPictures() {
        return pictures;
    }

    /**
     * @param pictures
     */
    public void setPictures(String pictures) {
        this.pictures = pictures == null ? null : pictures.trim();
    }

    /**
     * @return specification
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * @param specification
     */
    public void setSpecification(String specification) {
        this.specification = specification == null ? null : specification.trim();
    }

    /**
     * @return service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service
     */
    public void setService(String service) {
        this.service = service == null ? null : service.trim();
    }

    /**
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color
     */
    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    /**
     * @return size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size
     */
    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    /**
     * @return shop_id
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * @param shopId
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", pictures='" + pictures + '\'' +
                ", specification='" + specification + '\'' +
                ", service='" + service + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", shopId=" + shopId +
                '}';
    }
}