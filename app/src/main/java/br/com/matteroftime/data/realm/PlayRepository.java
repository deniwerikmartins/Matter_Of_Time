package br.com.matteroftime.data.realm;

import java.util.List;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.play.PlayContract;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by RedBlood on 02/04/2017.
 */

public class PlayRepository implements PlayContract.Repository{


    @Override
    public List<Musica> getAllMusics() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Musica> musicas = realm.where(Musica.class).findAllSorted("ordem");
        List<Musica> result = realm.copyFromRealm(musicas);
        realm.close();
        return result;
    }


}
