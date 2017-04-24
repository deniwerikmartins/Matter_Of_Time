package br.com.matteroftime.data.realm;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.uploadMusic.UploadMusicContract;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by RedBlood on 23/04/2017.
 */

public class UploadMusicRepository implements UploadMusicContract.Repository{
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
    public void salvaMusica(Musica musica, final Context context, final OnDatabaseOperationCompleteListener listener) {
        final File file = new File("data/data/br.com.matteroftime/"+musica.getNome()+"_music.met");
        String nome = musica.getNome();
        Future<File> uploading;

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(musica);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            /*Ion.with(context)
                    .load("http://matteroftime.com.br/inserir.php")
                    .setMultipartParameter("nome", nome)
                    .setMultipartFile("archive", "application/met", file)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (result.get("retorno").getAsString().equals("YES")) {
                                listener.onSQLOperationSucceded(context.getString(R.string.sucesso_envio));
                            } else {
                                listener.onSQLOperationSucceded(context.getString(R.string.erro_envio));
                            }
                        }
                    });*/

            File echoedFile = context.getFileStreamPath("echo");

            uploading = Ion.with(context)
                    .load("http://matteroftime.com.br/inserir.php")
                    .setMultipartParameter("nome", nome)
                    .setMultipartFile("archive", file)
                    .write(echoedFile)
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File result) {
                            if (e != null){
                                listener.onSQLOperationFailed(context.getString(R.string.erro_envio));
                            } else {
                                listener.onSQLOperationSucceded(context.getString(R.string.sucesso_envio));
                            }
                        }
                    });
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        file.delete();
        uploading = null;
    }

    @Override
    public void atualizaMusica(Musica musica, final Context context, final OnDatabaseOperationCompleteListener listener) {
        final File file = new File("data/data/br.com.matteroftime/"+musica.getNome()+"_music.met");
        String nome = musica.getNome();
        long id = musica.getId();
        Future<File> uploading;

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(musica);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            File echoedFile = context.getFileStreamPath("echo");

            uploading = Ion.with(context)
                    .load("http://matteroftime.com.br/inserir.php")
                    .setMultipartParameter("nome", nome)
                    .setMultipartParameter("id", String.valueOf(id))
                    .setMultipartFile("archive", file)
                    .write(echoedFile)
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File result) {
                            if (e != null){
                                listener.onSQLOperationFailed(context.getString(R.string.erro_envio));
                            } else {
                                listener.onSQLOperationSucceded(context.getString(R.string.sucesso_envio));
                            }
                        }
                    });


        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        file.delete();
        uploading = null;
    }
}
