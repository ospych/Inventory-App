package com.example.inventoryapp.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class ItemDb {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String supplier;
    private int quantity;
    private double price;
    private String image;

    public ItemDb( String image, String title, String supplier, double price, int quantity) {
        this.image = image;
        this.title = title;
        this.supplier = supplier;
        this.price = price;
        this.quantity = quantity;
    }


    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
