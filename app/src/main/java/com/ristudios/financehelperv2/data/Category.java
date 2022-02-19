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
        switch (this) {
            case NONE:
                return context.getString(R.string.category_none);
            case WORK_EDUCATION:
                return context.getString(R.string.category_work_school);
            case HYGIENE_COSMETICS:
                return context.getString(R.string.category_hygiene_cosmetics);
            case HOBBY:
                return context.getString(R.string.category_hobby);
            case GENERAL_ITEMS:
                return context.getString(R.string.category_general);
            case GROCERIES:
                return context.getString(R.string.category_groceries);
            case MONTHLY_PAYMENTS:
                return context.getString(R.string.category_monthly_payments);
            case LUXURY:
                return context.getString(R.string.category_luxury);

        }

        return "";
    }
}
