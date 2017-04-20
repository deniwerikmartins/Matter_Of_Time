package br.com.matteroftime.data.realm;

import java.util.List;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.userArea.UserAreaContract;

/**
 * Created by RedBlood on 20/04/2017.
 */

public class UserAreaRepository implements UserAreaContract.Repository {



    @Override
    public List<Musica> getAllMusics() {
        return null;
    }

    @Override
    public Musica getMusicById(long id) {
        return null;
    }

    @Override
    public void baixaMusica(Musica musica, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public void deleteMusic(Musica musica, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public void updateMusic(Musica musica, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public void enviaMusica(Musica musica, OnDatabaseOperationCompleteListener listener) {

    }
}
