package br.com.matteroftime.core.dagger;

import android.content.Context;

import br.com.matteroftime.data.realm.EditRepository;
import br.com.matteroftime.data.realm.LoginLogoutRepository;
import br.com.matteroftime.data.realm.PlayRepository;
import br.com.matteroftime.data.realm.SignUpRepository;
import br.com.matteroftime.data.realm.UploadMusicRepository;
import br.com.matteroftime.data.realm.UserAreaRepository;
import br.com.matteroftime.ui.edit.EditContract;
import br.com.matteroftime.ui.loginlogout.LoginLogoutContract;
import br.com.matteroftime.ui.play.PlayContract;
import br.com.matteroftime.ui.signup.SignUpContract;
import br.com.matteroftime.ui.uploadMusic.UploadMusicContract;
import br.com.matteroftime.ui.userArea.UserAreaContract;
import dagger.Module;
import dagger.Provides;

/**
 * Created by RedBlood on 02/04/2017.
 */
@Module
public class PersistenceModule {

    @Provides
    public PlayContract.Repository providesPlayRepository(Context context){
        return new PlayRepository();
    }

    @Provides
    public EditContract.Repository providesEditRepository(Context context){
        return new EditRepository();
    }

    @Provides
    public UserAreaContract.Repository providesUserAreaRepository(Context context){
        return new UserAreaRepository();
    }

    @Provides
    public UploadMusicContract.Repository providesUploadMusicRepository(Context context){
        return new UploadMusicRepository();
    }

    @Provides
    public SignUpContract.Repository providesSignUpRepository(Context context){
        return new SignUpRepository();
    }

    @Provides
    public LoginLogoutContract.Repository providesLoginLogOutRepository(Context context){
        return new LoginLogoutRepository();
    }
}
