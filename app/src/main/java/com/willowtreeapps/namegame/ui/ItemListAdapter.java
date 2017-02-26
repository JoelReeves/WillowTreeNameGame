package com.willowtreeapps.namegame.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.network.api.model.Item;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListViewHolder> {

    public interface ItemClickListener {
        void onClick(@NonNull Item item);
    }

    private final List<Item> itemList;
    private ItemClickListener itemClickListener;

    public ItemListAdapter(@NonNull List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new ItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {
        holder.setItemViewHolderListener(itemViewHolderListener);
        holder.bindItem(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private final ItemListViewHolder.ItemViewHolderListener itemViewHolderListener = new ItemListViewHolder.ItemViewHolderListener() {
        @Override
        public void onItemClick(int position) {
            if (itemClickListener != null) {
                itemClickListener.onClick(itemList.get(position));
            }
        }
    };
}
