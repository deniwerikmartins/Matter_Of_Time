package br.com.matteroftime.ui.uploadMusic;

import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 23/04/2017.
 */

public interface UploadMusicContract {

    interface View{

    }

    interface Action{

        void checkStatus(long id);
        Musica getMusica(long id);

    }

    interface Repository{

        Musica getMusicById(long id);


    }
}
