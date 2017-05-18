package br.com.matteroftime.ui.addMusic;

import android.content.Context;

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
    private Context editContext;
    @Inject EditContract.Repository repository;
    @Inject Bus bus;


    public AddMusicPresenter(AddMusicContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        bus.register(this);
    }



    @Override
    public void ondAddMusicButtonClick(Musica musica, Context context) {
        if (musicaId > 0){
            musica.setId(musicaId);
            updateMusic(musica, context);
        } else {
            saveMusic(musica, context);
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
    public void saveMusic(Musica musica, Context context) {

        RealmList<Compasso> compassos = new RealmList<>();

        for (int i = 0; i < musica.getQtdCompassos(); i++){
            compassos.add(i, new Compasso());
        }

        musica.setCompassos(null);
        musica.setCompassos(compassos);


        repository.addMusic(musica, this, context);

    }

    @Override
    public void updateMusic(Musica musica, Context context) {
        musica.setId(musicaId);
        repository.updateMusic(musica, this, context);
    }

  /*  @Override
    public void recebeContext(Context context) {
        editContext = context;
    }*/

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
