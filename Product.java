package com.example.helloworld.ouhkfyp;

/**
 * Created by leelaiyin on 16/1/2017.
 */

public class Product {
    public String product_id = "";
    public String category_id = "";
    public String name = "";
    public String description = "";
    public String brand = "";
    public String origin = "";
    public String image = "";
    public String numOfView = "";

    public Product(String product_id, String category_id, String name, String description, String brand, String origin, String image, String numOfView){
        this.product_id = product_id;
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.origin = origin;
        this.image = image;
        this.numOfView = numOfView;

    }
}