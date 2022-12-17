package com.example.CommercePlatform.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "title", length = 100, nullable = false, columnDefinition = "varchar(100)")
    @NotEmpty(message = "Введите наименование товара")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Введите описание товара")
    private String description;

    @Column(name = "price", length = 10, nullable = false, columnDefinition = "int")
    @NotNull(message = "Введите цену товара")
    @Min(value = 100, message = "Не продаём товары дешевле 100 рублей")
    private int price;

    @Column(name = "warehouse", nullable = false, columnDefinition = "varchar(100)")
    @NotEmpty(message = "Укажите склад")
    private String warehouse;

    @Column(name = "supplier", length = 50, nullable = false, columnDefinition = "varchar(50)")
    @NotEmpty(message = "Укажите поставщика")
    private String supplier;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Image> imageList = new ArrayList<>();

    @ManyToOne(optional = false)
    private ProductCategory category;

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList;

    @OneToMany(mappedBy = "product")
    private List<Order> orderList;

    public Product() {}

    public Product(String title, String description, int price, String warehouse, String supplier) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.supplier = supplier;
    }

    public void addImageToProduct(Image image) {
        image.setProduct(this);
        imageList.add(image);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }
}
