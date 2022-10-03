package com.ristudios.financehelperv2.data.monthlypayments;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ristudios.financehelperv2.data.Category;

@Entity(tableName = "monthlyPayments")
public class MonthlyPayment {

    @PrimaryKey
    @NonNull
    private String uuid;
    private String name;
    private Category category;
    private float price;

    public MonthlyPayment(@NonNull String uuid, String name, float price){
        this.uuid = uuid;
        this.name = name;
        this.category = Category.MONTHLY_PAYMENTS;
        this.price = price;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}