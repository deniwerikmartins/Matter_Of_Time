package br.com.matteroftime.ui.userArea;

import android.content.Context;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.selectMusic.SelectMusicDialogFragment;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class UserAreaPresenter implements UserAreaContract.Actions, OnDatabaseOperationCompleteListener{

    private final UserAreaContract.View view;
    @Inject UserAreaContract.Repository repository;
    @Inject Bus bus;

    public UserAreaPresenter(UserAreaContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        bus.register(this);
    }



    @Override
    public Musica getMusica(long id) {
        return repository.getMusicById(id);
    }

    @Override
    public void pesquisaMusica(String nomeMusica, Context context) {
        repository.pesquisaMusica(nomeMusica, this, context, this);
        /*List<Musica> availableMusics = new ArrayList<>();
        if (availableMusics != null && availableMusics.size() > 0){
            view.hideEmptyText();
            view.showMusicas(availableMusics);
        } else {
            view.showEmptyText();
        }*/
    }

    @Override
    public void recebeListagemMusicas(List<Musica> listagemMusicas) {
        if (listagemMusicas != null && listagemMusicas.size() > 0){
            view.hideEmptyText();
            view.showMusicas(listagemMusicas);
        } else {
            view.showEmptyText();
        }

    }


    @Override
    public void onSQLOperationFailed(String error) {
        view.showMessage(error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        view.showMessage(message);
    }
}
