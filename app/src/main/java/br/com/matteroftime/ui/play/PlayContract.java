package br.com.matteroftime.ui.play;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import java.util.List;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by RedBlood on 30/03/2017.
 */

public interface PlayContract {
    public interface View{
        public void mostrarMusicas(List<Musica> musicas);
        void showEmptyText();
        void hideEmptyText();
        void showMessage(String message);
        void atualizaViewsMusica(int i);


        void recebeMusica(Musica musica);
    }

    public interface Actions{
        public void loadMusics();
        public void play(Context context, Handler handler);
        public void stop();
        public void defineMusica(Musica musicaSelecionada);
        public void criaCompasso(int bpm, int tempos, int nota);
    }

    public interface Repository{
        public List<Musica> getAllMusics();


    }
}
