package br.com.matteroftime.ui.play;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.events.MusicListChangedEvent;
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
    Musica musicaCompasso;
    Thread thread;


    public PlayPresenter(PlayContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        bus.register(this);
    }

    @Override
    public void loadMusics() {
        List<Musica> musicasDisponiveis = repository.getAllMusics();
        if (musicasDisponiveis != null && musicasDisponiveis.size() > 0){
            view.hideEmptyText();
            view.mostrarMusicas(musicasDisponiveis);
        } else {
            view.showEmptyText();
        }
    }

    @Override
    public void play(Context context) {
        if (musica == null){
            view.showMessage(context.getString(R.string.sem_musica));
        } else if (musica.getNome() == ""){
            view.showMessage(context.getString(R.string.sem_musica));
        } else {

            musica.defineIntervalo(musica.getCompassos());

            play = new Play(context, this, musica);
            play.setPriority(Thread.MAX_PRIORITY);

            /*thread = new Thread(play);
            thread.setPriority(Thread.MAX_PRIORITY);*/

            play.start();
            //thread.start();

        }



    }

    @Override
    public void stop() {
        if (thread != null){
            //play.cancel();
            //thread.interrupt();
            //thread.stop();
            //thread.destroy();

            //play.cancel();
            //play.interrupt();
            //play.stop();
            play.destroy();

        } else {
            return;
        }

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
        RealmList<Compasso> compassos = new RealmList<>();
        compassos.add(compasso);
        musica = new Musica();
        musica.setCompassos(compassos);
        musica.setCompasso(true);
        this.defineMusica(musica);
        bus.post(new MusicListChangedEvent());
    }

    @Subscribe
    public void onMusicListChanged(MusicListChangedEvent event){
        loadMusics();
    }
}
