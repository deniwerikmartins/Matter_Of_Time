package br.com.matteroftime.ui.uploadMusic;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.inject.Inject;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import io.realm.RealmList;

/**
 * Created by RedBlood on 23/04/2017.
 */

public class UploadMusicPresenter implements UploadMusicContract.Action, OnDatabaseOperationCompleteListener{

    private final UploadMusicContract.View view;
    private View rootView;
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
                view.setEditMode(true);
            }
        }
    }

    @Override
    public Musica getMusica(long id) {
        return repository.getMusicById(id);
    }


    @Override
    public void enviaMusica(Musica musica, final Context context, long usuarioId) {
        RealmList<Compasso> comps = new RealmList<>();
        comps = musica.getCompassos();
        musica.setCompassos(null);
        musica.setCompassos(comps);
        if (view.isEditMode()){
            atualizaMusica(musica, context, usuarioId);
        } else {
            salvaMusica(musica, context, usuarioId);
        }
    }

    @Override
    public void salvaMusica(Musica musica, Context context, long usuarioId) {
        //musica.setCompassos(null);
        repository.salvaMusica(musica, context, this, usuarioId);

    }

    @Override
    public void atualizaMusica(Musica musica, Context context, long usuarioId) {
        repository.atualizaMusica(musica, context, this, usuarioId);
    }

    @Override
    public void onSQLOperationFailed(String error) {
        view.showMessage("error" + error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        view.showMessage(message);
    }
}
