package com.ristudios.financehelperv2.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a shoppingitem.
 */
@Entity(tableName = "items")
public class Item {

    @PrimaryKey
    @NonNull
    private String uuid;
    private String name;
    private Category category;
    private long dateMillis;
    private float price;
    private float priceTotal;
    private int count;
    private boolean isIncome;

    public Item (@NonNull String uuid, String name, Category category, long dateMillis, float price, float priceTotal, int count, boolean isIncome)
    {
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        this.dateMillis = dateMillis;
        this.price = price;
        this.priceTotal = priceTotal;
        this.count = count;
        this.isIncome = isIncome;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(float priceTotal) {
        this.priceTotal = priceTotal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
