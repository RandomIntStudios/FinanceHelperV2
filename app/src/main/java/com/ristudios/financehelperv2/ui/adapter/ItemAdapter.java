package com.ristudios.financehelperv2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> implements ItemViewHolder.ViewHolderClickListener {

    private ArrayList<Item> items;
    private AdapterClickListener listener;
    private Context context;

    public ItemAdapter(AdapterClickListener listener, Context context){
        this.listener = listener;
        this.context = context;
        items = new ArrayList<>();

    }

    @NonNull
    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(v, context, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bindView(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(ArrayList<Item> newList){
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public void onItemLayoutClicked(int position) {
        listener.onItemLayoutClicked(items.get(position), position);
    }

    @Override
    public void onItemLayoutLongClicked(int position) {
        listener.onItemLayoutLongClicked(items.get(position), position);
    }

    public interface AdapterClickListener{
        void onItemLayoutClicked(Item item, int pos);
        void onItemLayoutLongClicked(Item item, int pos);
    }
}
