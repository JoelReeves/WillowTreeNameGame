package com.willowtreeapps.namegame.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.network.api.model.Item;
import com.willowtreeapps.namegame.util.CircleBorderTransform;
import com.willowtreeapps.namegame.util.Ui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.willowtreeapps.namegame.ui.NameGameBaseActivity.FORWARD_SLASHES;
import static com.willowtreeapps.namegame.ui.NameGameBaseActivity.HTTP_PREFIX;

public class ItemListViewHolder extends RecyclerView.ViewHolder {

    public interface ItemViewHolderListener {
        void onItemClick(int position);
    }

    private static final int IMAGE_SIZE = 50;

    @BindView(R.id.employee_picture) ImageView employeeImage;
    @BindView(R.id.employee_name) TextView employeeName;

    private final Context context;
    private ItemViewHolderListener itemViewHolderListener;

    public ItemListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        context = itemView.getContext();
    }

    @OnClick(R.id.employee_container)
    protected void itemClick() {
        if (itemViewHolderListener != null) {
            itemViewHolderListener.onItemClick(getAdapterPosition());
        }
    }

    public void bindItem(@NonNull Item item) {
        employeeName.setText(item.getWholeName());

        final int imageSize = (int) Ui.convertDpToPixel(IMAGE_SIZE, context);
        final String headshotUrl = item.getHeadshot().getUrl();

        if (TextUtils.isEmpty(headshotUrl)) {
            Picasso.with(context)
                    .load(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(imageSize, imageSize)
                    .transform(new CircleBorderTransform())
                    .into(employeeImage);
        } else {
            Picasso.with(context)
                    .load(headshotUrl.replace(FORWARD_SLASHES, HTTP_PREFIX))
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(imageSize, imageSize)
                    .transform(new CircleBorderTransform())
                    .into(employeeImage);
        }
    }

    public void setItemViewHolderListener(ItemViewHolderListener itemViewHolderListener) {
        this.itemViewHolderListener = itemViewHolderListener;
    }
}
