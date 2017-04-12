package br.com.matteroftime.ui.addMusic;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.events.MusicListChangedEvent;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.edit.EditContract;
import io.realm.RealmList;

/**
 * Created by RedBlood on 10/04/2017.
 */

public class AddMusicPresenter implements AddMusicContract.Action, OnDatabaseOperationCompleteListener {

    private final AddMusicContract.View view;
    private long musicaId = 0;
    @Inject EditContract.Repository repository;
    @Inject Bus bus;


    public AddMusicPresenter(AddMusicContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        bus.register(this);
    }



    @Override
    public void ondAddMusicButtonClick(Musica musica) {
        if (musicaId > 0){
            musica.setId(musicaId);
            updateMusic(musica);
        } else {
            saveMusic(musica);
        }

    }

    @Override
    public void checkStatus(long id) {
        if (id > 0){
            if (repository.getMusicById(id) != null){
                musicaId = id;
                view.setEditMode(true);
                view.populateForm(repository.getMusicById(id));
            }
        }
    }

    @Override
    public void saveMusic(Musica musica) {

        RealmList<Compasso> compassos = new RealmList<>();

        for (int i = 0; i < musica.getQtdCompassos(); i++){
            compassos.add(i, new Compasso());
        }

        musica.setCompassos(compassos);

        if (musicaId > 0){
            musica.setId(musicaId);
            updateMusic(musica);
        } else {
            repository.addMusic(musica, this);
        }
    }

    @Override
    public void updateMusic(Musica musica) {
        musica.setId(musicaId);
        repository.updateMusic(musica, this);
    }


    @Override
    public void onSQLOperationFailed(String error) {
        view.displayMessage("Error: " + error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        view.displayMessage(message);
        bus.post(new MusicListChangedEvent());
    }
}
