package com.willowtreeapps.namegame.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.network.api.model.Item;
import com.willowtreeapps.namegame.ui.adapters.viewholders.ItemListViewHolder;

import java.util.Collections;
import java.util.Comparator;
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

    public void sortByFirstName() {
        Collections.sort(itemList, firstNamesAscendingComparator);
        updateRange();
    }

    public void sortByLastName() {
        Collections.sort(itemList, lastNamesAscendingComparator);
        updateRange();
    }

    public void shuffleNames() {
        Collections.shuffle(itemList);
        updateRange();
    }

    private void updateRange() {
        notifyItemRangeChanged(0, itemList.size());
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

    private final Comparator<Item> firstNamesAscendingComparator = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
        }
    };

    private final Comparator<Item> lastNamesAscendingComparator = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getLastName().compareToIgnoreCase(o2.getLastName());
        }
    };
}
