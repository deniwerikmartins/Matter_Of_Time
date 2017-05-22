package br.com.matteroftime.ui.downloadMusic;

import android.content.Context;

import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.events.MusicListChangedEvent;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.edit.EditFragment;

/**
 * Created by RedBlood on 12/05/2017.
 */

public class DownloadMusicPresenter implements DownloadMusicContract.Action, OnDatabaseOperationCompleteListener{

    private final DownloadMusicContract.View view;
    private EditFragment editFragment;
    @Inject DownloadMusicContract.Repository repository;
    @Inject Bus bus;
    private Musica musica;

    public DownloadMusicPresenter(DownloadMusicContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        //bus.register(this);
        EditFragment editFragment = new EditFragment();

        //bus.register(editFragment);
    }


    @Override
    public void onSQLOperationFailed(String error) {
        view.displayMessage("error " + error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        view.displayMessage(message);
    }

    @Override
    public void downloadMusica(Musica musica, Context context, EditFragment editFr) {
        //bus.unregister(this);
        editFragment = editFr;
        bus.register(editFragment);
        repository.downloadMusica(musica, this, context);
        loadMusics();
        bus.post(new MusicListChangedEvent());
    }

    @Override
    public void recebeEditFragment(EditFragment editFragment) {
        this.editFragment = editFragment;
    }

    @Override
    public void loadMusics() {
        List<Musica> availableMusics = repository.getAllMusics();
        if (availableMusics != null && availableMusics.size() > 0){
            view.showMusics(availableMusics);
        }

    }
}
