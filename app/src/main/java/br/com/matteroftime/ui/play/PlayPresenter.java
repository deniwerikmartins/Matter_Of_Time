package br.com.matteroftime.ui.play;

import android.content.Context;

import com.squareup.otto.Bus;

import java.util.List;

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
    @Inject
    Bus bus;

    Play play;
    Musica musica;


    public PlayPresenter(PlayContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        bus.register(this);
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
    public void play(Context context) {
        play = new Play(context, this);
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

    @Override
    public void criaCompasso(int bpm, int tempos, int nota) {
        Compasso compasso = new Compasso();
        compasso.setTempos(tempos);
        compasso.setNota(nota);
        compasso.setBpm(bpm);
        compasso.setRepeticoes(20);
        compasso.setId(1);
        RealmList<Compasso> compassos = new RealmList<>();
        compassos.add(compasso);
        Musica musica = new Musica();
        musica.setCompassos(compassos);
        musica.defineIntervalo(musica.getCompassos());
        this.defineMusica(musica);

    }
}
