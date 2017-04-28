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
        List<Musica> result = realm.copyFromRealm(musicas);
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
    public void atualizaCompasso(final Musica musica, final OnDatabaseOperationCompleteListener listener,final Compasso compasso) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                Musica managedMusic = backgroundRealm.where(Musica.class).equalTo("id", musica.getId()).findFirst();
                Compasso managedCompasso = managedMusic.getCompassos().get(compasso.getOrdem());

                managedCompasso.setOrdem(compasso.getOrdem());
                managedCompasso.setRepeticoes(compasso.getRepeticoes());
                managedCompasso.setNota(compasso.getNota());
                managedCompasso.setBpm(compasso.getBpm());
                managedCompasso.setTempos(compasso.getTempos());

                /*managedMusic.getCompassos().get(compasso.getOrdem()).setOrdem(compasso.getOrdem());
                managedMusic.getCompassos().get(compasso.getOrdem()).setNota(compasso.getNota());
                managedMusic.getCompassos().get(compasso.getOrdem()).setBpm(compasso.getBpm());
                managedMusic.getCompassos().get(compasso.getOrdem()).setTempos(compasso.getTempos());
                managedMusic.getCompassos().get(compasso.getOrdem()).setRepeticoes(compasso.getRepeticoes());*/

                //backgroundRealm.copyToRealmOrUpdate(managedMusic);
            }
        });

        /*realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm backgroundRealm) {
                                              Musica managedMusic = backgroundRealm.where(Musica.class).equalTo("id", musica.getId()).findFirst();
                                              managedMusic.getCompassos().get(compasso.getOrdem()).setBpm(compasso.getBpm());
                                              managedMusic.getCompassos().get(compasso.getOrdem()).setTempos(compasso.getTempos());
                                              managedMusic.getCompassos().get(compasso.getOrdem()).setNota(compasso.getNota());
                                              managedMusic.getCompassos().get(compasso.getOrdem()).setBpm(compasso.getBpm());
                                              managedMusic.getCompassos().get(compasso.getOrdem()).setRepeticoes(compasso.getRepeticoes());

                                              backgroundRealm.copyToRealmOrUpdate(managedMusic);
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
        );*/
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
/*        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                musica.getCompassos().get(0).setId(1);
                musica.getCompassos().get(1).setId(2);
                backgroundRealm.copyToRealmOrUpdate(musica.getCompassos());
                backgroundRealm.copyToRealmOrUpdate(musica);
            }
        });*/
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm backgroundRealm) {
                                              backgroundRealm.copyToRealmOrUpdate(musica);
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
