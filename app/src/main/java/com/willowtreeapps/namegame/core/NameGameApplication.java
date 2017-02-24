package com.willowtreeapps.namegame.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.willowtreeapps.namegame.BuildConfig;
import com.willowtreeapps.namegame.network.NetworkModule;

import timber.log.Timber;

public class NameGameApplication extends Application {

    private ApplicationComponent component;

    public static NameGameApplication get(@NonNull Context context) {
        return (NameGameApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public ApplicationComponent component() {
        return component;
    }

    protected ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule("https://willowtreeapps.com/"))
                .build();
    }
}
