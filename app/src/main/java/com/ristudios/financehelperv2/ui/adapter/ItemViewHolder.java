package com.ristudios.financehelperv2.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.utils.Utils;

import org.jetbrains.annotations.NotNull;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private TextView txtName, txtCount, txtPrice, txtPriceTotal, txtDate, txtTime;
    private ConstraintLayout constraintLayout;
    private Context context;
    private ViewHolderClickListener listener;

    public ItemViewHolder(@NonNull @NotNull View itemView, Context context, ViewHolderClickListener listener) {
        super(itemView);
        this.listener = listener;
        this.context = context;
        txtName = itemView.findViewById(R.id.txt_item_layout_name);
        txtCount = itemView.findViewById(R.id.txt_item_layout_count);
        txtPrice = itemView.findViewById(R.id.txt_item_layout_price);
        txtDate = itemView.findViewById(R.id.txt_item_layout_date);
        txtTime = itemView.findViewById(R.id.txt_item_layout_time);
        txtPriceTotal = itemView.findViewById(R.id.txt_item_layout_price_total);
        constraintLayout = itemView.findViewById(R.id.item_layout_constraint);
    }

    public void bindView(Item item){
        txtName.setText(item.getName());
        txtCount.setText( context.getResources().getString(R.string.multiply).replace("$COUNT", String.valueOf(item.getCount())));
        txtPrice.setText(context.getResources().getString(R.string.single_price).replace("$PRICE", String.valueOf(item.getPrice())));
        txtPriceTotal.setText(context.getResources().getString(R.string.total_price).replace("$TOTAL", String.valueOf(item.getPriceTotal())));
        txtDate.setText(Utils.getLocalizedFormattedDate(item.getDateMillis()));
        txtTime.setText(Utils.getLocalizedFormattedTime(item.getDateMillis()));
        if (item.isIncome()){
            constraintLayout.setBackground(ResourcesCompat.getDrawable(context.getResources() ,R.drawable.background_income, null));
        }
        else {
            constraintLayout.setBackground(ResourcesCompat.getDrawable(context.getResources() ,R.drawable.background_outgoings, null));
        }
        constraintLayout.setOnClickListener(v -> {
            listener.onItemLayoutClicked(getAdapterPosition());
        });
        constraintLayout.setOnLongClickListener(v -> {
            listener.onItemLayoutLongClicked(getAdapterPosition());
            return false;
        });
    }

    public interface ViewHolderClickListener{
        void onItemLayoutClicked(int position);
        void onItemLayoutLongClicked(int position);
    }
}
