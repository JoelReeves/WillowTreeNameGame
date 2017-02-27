package com.willowtreeapps.namegame.core;

import com.willowtreeapps.namegame.network.NetworkModule;
import com.willowtreeapps.namegame.ui.fragments.EmployeeListFragment;
import com.willowtreeapps.namegame.ui.fragments.NameGameFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class
})
public interface ApplicationComponent {
    void inject(NameGameFragment fragment);
    void inject(EmployeeListFragment fragment);
}