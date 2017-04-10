package br.com.matteroftime.core.dagger;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by RedBlood on 10/04/2017.
 */
@Module
public class BusModule {

    @Provides
    @Singleton
    public Bus provideBus(){
        return new Bus();
    }

}
