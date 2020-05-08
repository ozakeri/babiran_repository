package Models;

import java.util.ArrayList;

/**
 * Created by Mohammad on 5/27/2017.
 */

public class Product {
    public String id;
    public String name;
    public String description;
    public String price;
    public String stock;
    public String count;
    public String dis_price;

    public String providerName;

    public int rate;
    public ArrayList<Feature> features;
    public ArrayList<Models.Image> images;
    private String category_id;

    public Product(String id, String name, String description, String price, String stock, String count, String dis_price
            , ArrayList<Models.Image> images,
                   ArrayList<Feature> features) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.count = count;
        this.dis_price = dis_price;
        this.images = images;
        this.features = features;
    }

    public Product(String category_id, String id, String name, String description, String price, String stock, String count, String dis_price
            , ArrayList<Models.Image> images,
                   ArrayList<Feature> features) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.count = count;
        this.dis_price = dis_price;
        this.images = images;
        this.features = features;
        this.category_id = category_id;
    }

    public Product(String id, String name, String description, String price, String stock, String count, String dis_price
            , ArrayList<Models.Image> images,
                   ArrayList<Feature> features, String providerName) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.count = count;
        this.dis_price = dis_price;
        this.images = images;
        this.features = features;
        this.providerName = providerName;
    }

    public Product() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDis_price() {
        return dis_price;
    }

    public void setDis_price(String dis_price) {
        this.dis_price = dis_price;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Feature> features) {
        this.features = features;
    }

    public ArrayList<Models.Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Models.Image> images) {
        this.images = images;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
