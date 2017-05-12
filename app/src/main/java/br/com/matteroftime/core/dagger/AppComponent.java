package br.com.matteroftime.core.dagger;

import javax.inject.Singleton;

import br.com.matteroftime.common.MainActivity;
import br.com.matteroftime.ui.addMusic.AddMusicPresenter;
import br.com.matteroftime.ui.downloadMusic.DownloadMusicPresenter;
import br.com.matteroftime.ui.edit.EditPresenter;
import br.com.matteroftime.ui.loginlogout.LoginLogoutFragment;
import br.com.matteroftime.ui.loginlogout.LoginLogoutPresenter;
import br.com.matteroftime.ui.play.PlayPresenter;
import br.com.matteroftime.ui.selectMusic.SelectMusicPresenter;
import br.com.matteroftime.ui.signup.SignUpFragment;
import br.com.matteroftime.ui.signup.SignUpPresenter;
import br.com.matteroftime.ui.uploadMusic.UploadMusicPresenter;
import br.com.matteroftime.ui.userArea.UserAreaPresenter;
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
    void inject(UserAreaPresenter userAreaPresenter);
    void inject(SelectMusicPresenter selectMusicPresenter);
    void inject(UploadMusicPresenter uploadMusicPresenter);
    void inject(SignUpFragment signUpFragment);
    void inject(LoginLogoutFragment loginLogoutFragment);
    void inject(SignUpPresenter signUpPresenter);
    void inject(LoginLogoutPresenter loginLogoutPresenter);
    void inject(DownloadMusicPresenter downloadMusicPresenter);
}
