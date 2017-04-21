package br.com.matteroftime.ui.selectMusic;


import java.util.List;

import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 30/03/2017.
 */

public interface SelectMusicContract {

    interface View{

        void displayMessage(String message);
        void showEmptyText();
        void hideEmptyText();
        void showMusicas(List<Musica> musicas);


    }

    interface Actions{

        void checkStatus(long id);
        void loadMusicas();

    }



}
