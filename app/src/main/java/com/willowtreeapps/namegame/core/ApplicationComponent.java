package com.willowtreeapps.namegame.core;

import com.willowtreeapps.namegame.network.NetworkModule;
import com.willowtreeapps.namegame.ui.EmployeeListFragment;
import com.willowtreeapps.namegame.ui.NameGameFragment;

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