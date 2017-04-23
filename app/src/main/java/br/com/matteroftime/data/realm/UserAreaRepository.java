package br.com.matteroftime.data.realm;

import java.util.List;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.userArea.UserAreaContract;
import br.com.matteroftime.ui.userArea.UserAreaPresenter;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by RedBlood on 20/04/2017.
 */

public class UserAreaRepository implements UserAreaContract.Repository {

    @Override
    public List<Musica> getAllMusics() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Musica> musicas = realm.where(Musica.class).findAllSorted("ordem");
        List<Musica> result = realm.copyFromRealm(musicas);
        realm.close();
        return result;
    }

    @Override
    public Musica getMusicById(long id) {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Musica> musicas = realm.where(Musica.class).equalTo("id", id).findAll();
            Musica result = musicas.first();
            Musica inMemoryMusic = realm.copyFromRealm(result);
            realm.close();
            return inMemoryMusic;
    }

    @Override
    public void pesquisaMusica(Musica musica, OnDatabaseOperationCompleteListener listener) {
        //pesquisa no mysql
    }

    @Override
    public void deletaMusica(Musica musica, OnDatabaseOperationCompleteListener listener) {
        //delete no mysql
    }

    @Override
    public void atualizaMusica(Musica musica, OnDatabaseOperationCompleteListener listener) {
        //atualiza no mysql
    }


    @Override
    public void enviaMusica(Musica musica, OnDatabaseOperationCompleteListener listener) {
        //mandar arquivo serializado
    }

    @Override
    public void baixaMusica(Musica musica, UserAreaPresenter userAreaPresenter) {
        //recebe arquivo serializado
    }
}
