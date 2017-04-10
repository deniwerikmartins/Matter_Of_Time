package br.com.matteroftime.ui.edit;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.events.MusicListChangedEvent;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class EditPresenter implements EditContract.Actions, OnDatabaseOperationCompleteListener{

    private final EditContract.View view;
    @Inject EditContract.Repository repository;
    @Inject
    Bus bus;

    public EditPresenter(EditContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        bus.register(this);
    }

    @Override
    public void loadMusics() {
        List<Musica> availableMusics = repository.getAllMusics();
        if (availableMusics != null && availableMusics.size() > 0){
            view.hideEmptyText();
            view.showMusics(availableMusics);
        } else {
            view.showEmptyText();
        }
    }

    @Override
    public Musica getMusica(long id) {
        return repository.getMusicById(id);
    }

    @Override
    public void onAddMusicButtonClicked() {
        view.showAddMusicForm();
    }

    @Override
    public void ondAddToEditButtonClicked(Musica musica) {
        //passar para a view
    }

    @Override
    public void addMusic(Musica musica) {
        repository.addMusic(musica, this);
    }

    @Override
    public void onDeleteMusicButtonClicked(Musica musica) {
        view.showDeleteMusicPrompt(musica);
    }

    @Override
    public void deleteMusic(Musica musica) {
        repository.deleteMusic(musica,this);
        loadMusics();
    }

    @Override
    public void onEditMusicaButtonClicked(Musica musica) {
        view.showEditMusicForm(musica);
    }

    @Override
    public void updateMusica(Musica musica) {
        repository.updateMusic(musica,this);
    }


    @Override
    public void onSQLOperationFailed(String error) {
        view.showMessage(error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        view.showMessage(message);
    }

    @Subscribe
    public void onMusicListChanged(MusicListChangedEvent event){
        loadMusics();
    }



}
