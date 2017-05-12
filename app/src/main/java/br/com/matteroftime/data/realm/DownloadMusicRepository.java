package br.com.matteroftime.data.realm;

import android.content.Context;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.downloadMusic.DownloadMusicContract;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by RedBlood on 12/05/2017.
 */

public class DownloadMusicRepository implements DownloadMusicContract.Repository {
    File file;
    File file2;
    Musica musica1 = new Musica();

    @Override
    public void downloadMusica(Musica musica, final OnDatabaseOperationCompleteListener listener, final Context context) {
        long musicaId = musica.getId();
        String id = String.valueOf(musicaId);
        /*JsonObject json = new JsonObject();
        json.addProperty("id", id);*/
        Future<File> downloading;

        downloading = Ion.with(context)
                .load("https://matteroftime-redblood666.c9users.io/download.php")
                //.setJsonObjectBody(json)
                .setBodyParameter("id", id)
                .write(context.getFileStreamPath("archive_"+System.currentTimeMillis()+"_music.met"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File result) {
                        if (e != null){
                            listener.onSQLOperationFailed(context.getString(R.string.erro_download));
                            return;
                        } else {
                            file = result;
                            ///////////////////////////////////////////////////////////////////////

                            try {
                                //file2 = (File)downloading;
                                //FileInputStream fileInputStream = new FileInputStream("data/data/br.com.matteroftime/"+musica.getNome()+"_music.met");
                                FileInputStream fileInputStream = new FileInputStream(file); // file ou file 2
                                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                                musica1 = (Musica) objectInputStream.readObject();
                                objectInputStream.close();
                                fileInputStream.close();
                                listener.onSQLOperationSucceded(context.getString(R.string.sucesso_importar));
                            } catch (FileNotFoundException ee) {
                                ee.printStackTrace();
                                listener.onSQLOperationFailed(context.getString(R.string.falha_importar));
                            } catch (IOException ee) {
                                ee.printStackTrace();
                                listener.onSQLOperationFailed(context.getString(R.string.falha_importar));
                            } catch (ClassNotFoundException ee) {
                                ee.printStackTrace();
                                listener.onSQLOperationFailed(context.getString(R.string.falha_importar));
                            }
                            ////////////////////////////////////////////////////////////////////////

                            final Realm realm = Realm.getDefaultInstance();
                            final long idMusica = MatterOfTimeApplication.musicaPrimarykey.incrementAndGet();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                                              @Override
                                                              public void execute(Realm backgroundRealm) {
                                                                  musica1.setId(idMusica);
                                                                  /*for (Compasso compasso : musica1.getCompassos()) {
                                                                      compasso.setId(MatterOfTimeApplication.compassoPrimarykey.incrementAndGet());
                                                                  }*/
                                                                  for (Compasso compasso : musica1.getCompassosList()){
                                                                      compasso.setId(MatterOfTimeApplication.compassoPrimarykey.getAndIncrement());
                                                                  }
                                                                  backgroundRealm.copyToRealmOrUpdate(musica1);
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





                            listener.onSQLOperationSucceded(context.getString(R.string.sucesso_baixar));
                        }

                    }
                });



    }

    @Override
    public List<Musica> getAllMusics() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Musica> musicas = realm.where(Musica.class).findAllSorted("ordem");
        List<Musica> result = realm.copyFromRealm(musicas);
        realm.close();
        return result;
    }
}
