package br.com.matteroftime.ui.edit;

import java.util.List;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.addMusic.AddMusicPresenter;

/**
 * Created by RedBlood on 30/03/2017.
 */

public interface EditContract {
    public interface View{
        void recebeMusica(Musica musica);
        void showMusics(List<Musica> musicas);
        void showAddMusicForm();
        void showEditMusicForm(Musica musica);
        void showDeleteMusicPrompt(Musica musica);
        void showEmptyText();
        void hideEmptyText();
        void showMessage(String message);
        void atualizaViewsMusica(Musica musica);
        void atualizaViewsCompasso(Musica musica, Compasso compasso);
        void atualizaNomeMusica(Musica musicaSelecionada);

        void resetaNome();
    }
    public interface Actions{
        void loadMusics();
        Musica getMusica(long id);
        void onAddMusicButtonClicked();
        void ondAddToEditButtonClicked(Musica musica);
        void addMusic(Musica musica);
        void onDeleteMusicButtonClicked(Musica musica);
        void deleteMusic(Musica musica);
        void onEditMusicaButtonClicked(Musica musica);
        void updateMusica(Musica musica);
        List<Musica> getListaMusicas();
        void atualizarCompassodaMusica(Musica musica, Compasso compasso);
        void atualizaMusica(Musica musica);
    }

    public interface Repository{
        List<Musica> getAllMusics();
        Musica getMusicById(long id);
        void deleteMusic(Musica musica, OnDatabaseOperationCompleteListener listener);
        void addMusic(Musica musica, OnDatabaseOperationCompleteListener listener);
        void updateMusic(Musica musica, OnDatabaseOperationCompleteListener listener);
        void atualizaCompasso(Musica musica, OnDatabaseOperationCompleteListener listener, Compasso compasso);
        void atualizaMusica(Musica musica);
    }
}
