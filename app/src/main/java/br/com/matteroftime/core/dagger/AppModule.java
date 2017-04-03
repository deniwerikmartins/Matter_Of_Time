package br.com.matteroftime.core.dagger;

import android.content.Context;

import javax.inject.Singleton;

import br.com.matteroftime.core.MatterOfTimeApplication;
import dagger.Module;
import dagger.Provides;

/**
 * Created by RedBlood on 02/04/2017.
 */
@Module
public class AppModule {
    private final MatterOfTimeApplication app;

    public AppModule(MatterOfTimeApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public MatterOfTimeApplication getApp() {
        return app;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return app;
    }
}
