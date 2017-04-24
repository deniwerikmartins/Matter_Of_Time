package br.com.matteroftime.ui.uploadMusic;

import android.content.Context;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 23/04/2017.
 */

public interface UploadMusicContract {

    interface View{

        void setEditMode(boolean editMode);
        void showMessage(String message);
        void displayMessage(String message);
        boolean isEditMode();


    }

    interface Action{

        void checkStatus(long id);
        Musica getMusica(long id);
        void enviaMusica(Musica musica, Context context);
        void salvaMusica(Musica musica, Context context);
        void atualizaMusica(Musica musica, Context context);


    }

    interface Repository{

        Musica getMusicById(long id);
        void salvaMusica(Musica musica, Context context, OnDatabaseOperationCompleteListener listener);
        void atualizaMusica(Musica musica, Context context, OnDatabaseOperationCompleteListener listener);



    }
}
