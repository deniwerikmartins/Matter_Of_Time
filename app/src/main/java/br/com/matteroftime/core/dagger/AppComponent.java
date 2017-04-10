package br.com.matteroftime.core.dagger;

import javax.inject.Singleton;

import br.com.matteroftime.common.MainActivity;
import br.com.matteroftime.ui.addMusic.AddMusicPresenter;
import br.com.matteroftime.ui.edit.EditPresenter;
import br.com.matteroftime.ui.play.PlayPresenter;
import dagger.Component;

/**
 * Created by RedBlood on 30/03/2017.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                PersistenceModule.class,
                BusModule.class
})
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(PlayPresenter presenter);
    void inject(AddMusicPresenter addMusicPresenter);
    void inject(EditPresenter editPresenter);
}
