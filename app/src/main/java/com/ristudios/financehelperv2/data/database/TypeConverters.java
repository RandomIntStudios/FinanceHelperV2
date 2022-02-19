package com.ristudios.financehelperv2.data.database;

import androidx.room.TypeConverter;

import com.ristudios.financehelperv2.data.Category;
import com.ristudios.financehelperv2.utils.Utils;

public class TypeConverters {

    @TypeConverter
    public static Category fromName(String name){
        switch (name){
            case "groceries" : return Category.GROCERIES;
            case "general": return Category.GENERAL_ITEMS;
            case "hobby": return Category.HOBBY;
            case "hygiene": return Category.HYGIENE_COSMETICS;
            case "luxury": return Category.LUXURY;
            case "monthly": return Category.MONTHLY_PAYMENTS;
            case "none": return Category.NONE;
            case "work": return Category.WORK_EDUCATION;
        }
        return Category.NONE;
    }

    @TypeConverter
    public static String categoryToString(Category category){
        return category.name();
    }

}
