package br.com.matteroftime.ui.downloadMusic;

import android.content.Context;

import java.util.List;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.userArea.UserAreaFragment;

/**
 * Created by RedBlood on 12/05/2017.
 */

public interface DownloadMusicContract {

    interface View{

        void showMessage(String message);
        void displayMessage(String message);
        void recebeUserAreaView(UserAreaFragment userAreaFragment, Context context);
        void showMusics(List<Musica> availableMusics);
    }

    interface Action{

        void downloadMusica(Musica musica, Context context);
        void loadMusics();

    }

    interface Repository{
        List<Musica> getAllMusics();
        void downloadMusica(Musica musica, OnDatabaseOperationCompleteListener listener, Context context);

    }
}
