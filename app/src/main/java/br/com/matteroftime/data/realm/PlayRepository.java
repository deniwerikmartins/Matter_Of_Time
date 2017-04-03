package br.com.matteroftime.data.realm;

import java.util.List;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.play.PlayContract;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by RedBlood on 02/04/2017.
 */

public class PlayRepository implements PlayContract.Repository{


    @Override
    public List<Musica> getAllMusics() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Musica> musicas = realm.where(Musica.class).findAll();
        List<Musica> result = realm.copyFromRealm(musicas);
        realm.close();
        return result;
    }

    @Override
    public void addMusic(final Musica musica, final OnDatabaseOperationCompleteListener listener) {
        final Realm realm = Realm.getDefaultInstance();

        final long id = MatterOfTimeApplication.musicaPrimarykey.incrementAndGet();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                //fazer for each nos compassos com "chave estrangeira" para a musica

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                listener.onSQLOperationSucceded("Added");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realm.close();
                listener.onSQLOperationFailed(error.getLocalizedMessage());
            }
        });
    }
}
