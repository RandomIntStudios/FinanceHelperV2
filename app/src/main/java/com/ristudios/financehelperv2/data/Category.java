package com.ristudios.financehelperv2.data;

import android.content.Context;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.utils.Utils;

public enum Category {

    NONE("none"),
    MONTHLY_PAYMENTS("monthly"),
    GROCERIES("groceries"),
    HYGIENE_COSMETICS("hygiene"),
    LUXURY("luxury"),
    GENERAL_ITEMS("general"),
    HOBBY("hobby"),
    WORK_EDUCATION("work");

    Category(String name){

    }

    public String getLocalizedName(Context context) {
        String[] categories = context.getResources().getStringArray(R.array.categories);
        switch (this) {
            case NONE:
                return categories[2];
            case WORK_EDUCATION:
                return categories[8];
            case HYGIENE_COSMETICS:
                return categories[4];
            case HOBBY:
                return categories[7];
            case GENERAL_ITEMS:
                return categories[6];
            case GROCERIES:
                return categories[3];
            case MONTHLY_PAYMENTS:
                return categories[1];
            case LUXURY:
                return categories[5];

        }

        return "";
    }
}
