package br.com.matteroftime.core;

import android.app.Application;

import java.util.concurrent.atomic.AtomicLong;

import br.com.matteroftime.core.dagger.AppComponent;
import br.com.matteroftime.core.dagger.AppModule;
import br.com.matteroftime.core.dagger.DaggerAppComponent;
import br.com.matteroftime.models.Musica;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * Created by RedBlood on 30/03/2017.
 */

public class MatterOfTimeApplication extends Application{

    public static AtomicLong musicaPrimarykey;

    private static MatterOfTimeApplication instance = new MatterOfTimeApplication();
    private static AppComponent appComponent;

    public static MatterOfTimeApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getAppComponent();
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("matter_of_time.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        Realm realm = Realm.getDefaultInstance();

        try {
            musicaPrimarykey = new AtomicLong(realm.where(Musica.class).max("id").longValue());
        } catch (Exception e){
            realm.beginTransaction();
            Musica musica = new Musica();
            musica.setId(0);
            realm.copyToRealm(musica);
            musicaPrimarykey = new AtomicLong(realm.where(Musica.class).max("id").longValue());
            RealmResults<Musica> results = realm.where(Musica.class).equalTo("id",0).findAll();
            results.deleteAllFromRealm();
            realm.commitTransaction();

        }
    }

    public AppComponent getAppComponent() {
        if (appComponent == null){
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;

    }
}
