package br.com.matteroftime.ui.uploadMusic;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 23/04/2017.
 */

public class UploadMusicPresenter implements UploadMusicContract.Action, OnDatabaseOperationCompleteListener{

    private final UploadMusicContract.View view;
    private long musicaId = 0;
    @Inject UploadMusicContract.Repository repository;
    @Inject Bus bus;
    private Musica musica;

    public UploadMusicPresenter(UploadMusicContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        bus.register(this);
    }

    @Override
    public void checkStatus(long id) {
        if (id > 0) {
            if (repository.getMusicById(id) != null){
                musicaId = id;
            }
        }
    }

    @Override
    public Musica getMusica(long id) {
        return repository.getMusicById(id);
    }

    @Override
    public void onSQLOperationFailed(String error) {

    }

    @Override
    public void onSQLOperationSucceded(String message) {

    }
}
