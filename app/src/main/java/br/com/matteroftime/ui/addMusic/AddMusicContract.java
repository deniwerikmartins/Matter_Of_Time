package br.com.matteroftime.ui.addMusic;

import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 10/04/2017.
 */

public interface AddMusicContract {
    interface View{
        void populateForm(Musica musica);
        void setEditMode(boolean editMode);
        void displayMessage(String message);

    }

    interface Action{
        void ondAddMusicButtonClick(Musica musica);
        void checkStatus(long id);
        void saveMusic(Musica musica);
        void updateMusic(Musica musica);
    }
}
