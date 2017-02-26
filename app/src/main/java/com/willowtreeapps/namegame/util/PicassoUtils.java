package com.willowtreeapps.namegame.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.willowtreeapps.namegame.R;

public final class PicassoUtils {

    private static final String FORWARD_SLASHES = "//";
    private static final String HTTP_PREFIX = "http://";

    private PicassoUtils() {
        throw new AssertionError("No instances.");
    }

    public static void loadImageFromUrl(Context context, String urlString, int size, ImageView imageView) {
        if (TextUtils.isEmpty(urlString)) {
            Picasso.with(context.getApplicationContext())
                    .load(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(size, size)
                    .transform(new CircleBorderTransform())
                    .into(imageView);
        } else {
            Picasso.with(context.getApplicationContext())
                    .load(urlString.replace(FORWARD_SLASHES, HTTP_PREFIX))
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(size, size)
                    .transform(new CircleBorderTransform())
                    .into(imageView);
        }
    }
}
