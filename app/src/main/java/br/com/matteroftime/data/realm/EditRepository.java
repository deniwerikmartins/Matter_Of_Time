package br.com.matteroftime.data.realm;

import java.util.List;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.edit.EditContract;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by RedBlood on 10/04/2017.
 */

public class EditRepository implements EditContract.Repository{

    @Override
    public List<Musica> getAllMusics() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Musica> musicas = realm.where(Musica.class).findAllSorted("ordem");
        //RealmResults<Musica> musicas = realm.where(Musica.class).findAll();
        List<Musica> result = realm.copyFromRealm(musicas);

        /*RealmResults<Compasso> compassos = realm.where(Compasso.class).findAll();
        List<Compasso> resultcomp = realm.copyFromRealm(compassos);*/

        realm.close();
        return result;
    }

    @Override
    public Musica getMusicById(long id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Musica> musicas = realm.where(Musica.class).equalTo("id", id).findAll();
        Musica result = musicas.first();
        Musica inMemoryMusic = realm.copyFromRealm(result);
        realm.close();
        return inMemoryMusic;
    }

    @Override
    public void atualizaCompasso(Musica musica, Compasso compasso){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        //final Compasso managedCompasso = realm.copyToRealm(compasso);
        final Compasso managedCompasso = realm.copyToRealmOrUpdate(compasso);
        musica.getCompassos().set(compasso.getOrdem(), managedCompasso);

        realm.copyToRealmOrUpdate(musica);

        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void atualizaMusica(Musica musica) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final Musica mangedMusica = realm.copyToRealmOrUpdate(musica);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void deleteMusic(final Musica musica, final OnDatabaseOperationCompleteListener listener) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm backgroundRealm) {
                                              Musica musicaToBeDeleted = backgroundRealm.where(Musica.class).equalTo("id", musica.getId()).findFirst();
                                              musicaToBeDeleted.deleteFromRealm();
                                          }
                                      }, new Realm.Transaction.OnSuccess() {
                                          @Override
                                          public void onSuccess() {
                                              realm.close();
                                              listener.onSQLOperationSucceded("Deleted");
                                          }
                                      }, new Realm.Transaction.OnError() {
                                          @Override
                                          public void onError(Throwable error) {
                                              realm.close();
                                              listener.onSQLOperationFailed(error.getLocalizedMessage());
                                          }
                                      }

        );
    }

    @Override
    public void addMusic(final Musica musica, final OnDatabaseOperationCompleteListener listener) {

        final Realm realm = Realm.getDefaultInstance();

        final long id = MatterOfTimeApplication.musicaPrimarykey.incrementAndGet();
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm backgroundRealm) {
                                              musica.setId(id);
                                              backgroundRealm.copyToRealmOrUpdate(musica);
                                          }
                                      }, new Realm.Transaction.OnSuccess() {
                                          @Override
                                          public void onSuccess() {
                                              realm.close();
                                              listener.onSQLOperationSucceded("Added");
                                          }
                                      }, new Realm.Transaction.OnError() {
                                          @Override
                                          public void onError(Throwable error) {
                                              realm.close();
                                              listener.onSQLOperationFailed(error.getLocalizedMessage());
                                          }
                                      }

        );


    }

    @Override
    public void updateMusic(final Musica musica, final OnDatabaseOperationCompleteListener listener) {

        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm backgroundRealm) {
                                              //musica.setId(99);
                                              backgroundRealm.copyToRealmOrUpdate(musica);
                                              /*for (Compasso compasso : musica.getCompassos()) {
                                                  compasso.setMusica(musica);
                                                  backgroundRealm.copyToRealm(compasso);
                                              }*/
                                              /*
                                              *  Dog dog = bgRealm.where(Dog.class).equalTo("age", 1).findFirst();
                                                dog.setAge(3);
                                              * */

                                          }
                                      }, new Realm.Transaction.OnSuccess() {
                                          @Override
                                          public void onSuccess() {
                                              realm.close();
                                              listener.onSQLOperationSucceded("Updated");
                                          }
                                      }, new Realm.Transaction.OnError() {
                                          @Override
                                          public void onError(Throwable error) {
                                              realm.close();
                                              listener.onSQLOperationFailed(error.getLocalizedMessage());
                                          }
                                      }

        );
    }
}
