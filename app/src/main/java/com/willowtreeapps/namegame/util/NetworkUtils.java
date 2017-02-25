package com.willowtreeapps.namegame.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

public final class NetworkUtils {

    private NetworkUtils() {
        throw new AssertionError("No instances.");
    }

    public static boolean networkIsAvailable(@NonNull Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null &&
                cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isAvailable() &&
                cm.getActiveNetworkInfo().isConnected();
    }
}
