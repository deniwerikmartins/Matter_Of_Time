package br.com.matteroftime.ui.userArea;

import android.content.Context;

import java.util.List;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 30/03/2017.
 */

public interface UserAreaContract {

    public interface View{

        void hideEmptyText();

        void showEmptyText();

        void showMessage(String message);

        void showMusicas(List<Musica> musicas);



    }

    public interface Actions{

        Musica getMusica(long id);
        void pesquisaMusica(String nomeMusica, Context context);
        void baixaMusica(Musica musica, Context context);


    }

    public interface Repository{

        List<Musica> getAllMusics();

        Musica getMusicById(long id);

        List<Musica> pesquisaMusica(String nomeMusica, OnDatabaseOperationCompleteListener listener, Context context);

        void baixaMusica(Musica musica, OnDatabaseOperationCompleteListener listener, Context context);
    }
}
