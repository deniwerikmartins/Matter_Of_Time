package br.com.matteroftime.ui.addMusic;

import android.content.Context;

import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 10/04/2017.
 */

public interface AddMusicContract {
    interface View{
        void populateForm(Musica musica);
        void setEditMode(boolean editMode);
        void displayMessage(String message);

        //void recebeContext(Context context);
    }

    interface Action{
        void ondAddMusicButtonClick(Musica musica, Context context);
        void checkStatus(long id);
        void saveMusic(Musica musica, Context context);
        void updateMusic(Musica musica, Context context);
        //void recebeContext(Context context);
    }
}
