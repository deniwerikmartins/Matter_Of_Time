package br.com.matteroftime.ui.play;

import android.content.Context;

import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import io.realm.RealmList;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class PlayPresenter implements PlayContract.Actions{
    private final PlayContract.View view;
    @Inject PlayContract.Repository repository;

    Play play;
    Musica musica;

    public PlayPresenter(PlayContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void loadMusics() {
        List<Musica> musicasDisponiveis = repository.getAllMusics();
        if (musicasDisponiveis != null && musicasDisponiveis.size() > 0){
            view.mostrarMusicas(musicasDisponiveis);
        } else {
            //exibir lista vazia (com mensagem??)
        }
    }

    @Override
    public void play() {
        //play = new Play((Context) view,this);
        play = new Play();
        musica.defineIntervalo(musica.getCompassos());
        play.execute(musica);
    }

    @Override
    public void stop() {
        play.cancel();
    }

    @Override
    public void defineMusica(Musica musicaSelecionada) {
        musica = musicaSelecionada;
    }
}
