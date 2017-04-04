package br.com.matteroftime.ui.play;

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
        public void criaCompasso();
        public void tocar();
        public void parar();

    }

    public interface Actions{
        public void loadMusics();
        public void play();
        public void stop();
        public void defineMusica(Musica musicaSelecionada);
    }

    public interface Repository{
        public List<Musica> getAllMusics();
        public void addMusic(Musica musica, OnDatabaseOperationCompleteListener listener);

    }
}
