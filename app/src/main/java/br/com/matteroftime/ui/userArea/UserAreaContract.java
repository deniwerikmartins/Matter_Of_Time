package br.com.matteroftime.ui.userArea;

import java.util.List;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 30/03/2017.
 */

public interface UserAreaContract {

    public interface View{

        void hideEmptyText();

        void showMusics(List<Musica> availableMusics);

        void showEmptyText();

        void showMessage(String message);


    }

    public interface Actions{

        void loadMusics();
        Musica getMusica(long id);
        void baixaMusica(Musica musica);
        void enviaMusica(Musica musica);
        void updateMusica(Musica musica);
        void deleteMusic(Musica musica);

    }

    public interface Repository{

        List<Musica> getAllMusics();

        Musica getMusicById(long id);

        void baixaMusica(Musica musica, OnDatabaseOperationCompleteListener listener);

        void deleteMusic(Musica musica, OnDatabaseOperationCompleteListener listener);

        void updateMusic(Musica musica, OnDatabaseOperationCompleteListener listener);

        void enviaMusica(Musica musica, OnDatabaseOperationCompleteListener listener);
    }
}
