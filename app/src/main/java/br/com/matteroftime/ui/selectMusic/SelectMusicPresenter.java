package br.com.matteroftime.ui.selectMusic;

import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.events.MusicListChangedEvent;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.userArea.UserAreaContract;
import br.com.matteroftime.ui.userArea.UserAreaPresenter;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class SelectMusicPresenter implements SelectMusicContract.Actions, OnDatabaseOperationCompleteListener{
    private final SelectMusicContract.View view;
    private long musicId = 0;
    @Inject UserAreaContract.Repository repository;
    @Inject Bus bus;

    public SelectMusicPresenter(SelectMusicContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        bus.register(this);
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

    @Override
    public void checkStatus(long id) {
        if (id > 0) {
            if (repository.getMusicById(id) != null){
                musicId = id;
                //view.setEditMode(true);
                //view.populateForm(repository.getMusicById(id));
            }
        }
    }

    @Override
    public void loadMusicas(){
        List<Musica> availableMusics = repository.getAllMusics();
        if (availableMusics != null && availableMusics.size() > 0){
            view.hideEmptyText();
            view.showMusicas(availableMusics);
        } else {
            view.showEmptyText();
        }
    }

}
