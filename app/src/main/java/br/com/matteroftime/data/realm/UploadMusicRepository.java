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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.uploadMusic.UploadMusicContract;
import br.com.matteroftime.util.Constants;
import io.realm.Realm;
import io.realm.RealmList;
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
    public void salvaMusica(Musica musica, final Context context, final OnDatabaseOperationCompleteListener listener, final long usuarioId){
        musica.setId(0);
        for (Compasso compasso: musica.getCompassos()) {
            compasso.setId(0);
        }

        String nome = musica.getNome();
        nome = nome.trim();
       /* nome = nome.toLowerCase();
        nome = nome.replaceAll(" ","");
        nome = Normalizer.normalize(nome, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");*/
        final File file = new File("data/data/br.com.matteroftime/"+nome+"_music.met");

        List<Compasso> compassosList = new ArrayList<>();

        for (Compasso compasso: musica.getCompassos()) {
            compassosList.add(compasso);
        }

        musica.setCompassosList(compassosList);
        musica.setCompassos(null);
        //Future<JsonObject> uploading;

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(musica);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            //File echoedFile = context.getFileStreamPath("echo");

            Future<JsonObject> jsonObjectFuture = Ion.with(context)
                    .load("https://matteroftime-redblood666.c9users.io/upload.php")
                    .setMultipartParameter(Constants.ID_USUARIO, String.valueOf(usuarioId))
                    .setMultipartParameter("musica", nome)
                    .setMultipartFile("caminho", file)
                    //.write(echoedFile)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            /*try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }*/
                            if (result == null) {
                                file.delete();
                                listener.onSQLOperationFailed(context.getString(R.string.erro_envio));
                            } else if (result.get("result").getAsString().equals("NO")) {
                                file.delete();
                                listener.onSQLOperationFailed(context.getString(R.string.erro_envio));
                            } else {
                                file.delete();
                                listener.onSQLOperationSucceded(context.getString(R.string.sucesso_envio));
                            }
                        }
                    });

        } catch (FileNotFoundException e){
            file.delete();
            e.printStackTrace();
        } catch (IOException e){
            file.delete();
            e.printStackTrace();
        }
        //file.delete();
        /*try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //uploading = null;
    }

    @Override
    public void atualizaMusica(Musica musica, final Context context, final OnDatabaseOperationCompleteListener listener, final long usuarioId) {
        final File file = new File("data/data/br.com.matteroftime/"+musica.getNome()+"_music.met");
        final String nome = musica.getNome();
        final long id = musica.getId();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.ID_MUSICA, musica.getId());


        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(musica);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            Ion.with(context)
                    .load("http://matteroftime.com.br/login.php")
                    .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (result.get("result").getAsString().equals("NO")){
                                listener.onSQLOperationFailed(context.getString(R.string.erro_envio));
                            } else {
                                File echoedFile = context.getFileStreamPath("echo");
                                Future<File> uploading;
                                uploading = Ion.with(context)
                                        .load("http://matteroftime.com.br/inserir.php")
                                        .setMultipartParameter("nome", nome)
                                        .setMultipartParameter(Constants.ID_MUSICA, String.valueOf(id))
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
                                uploading = null;
                            }
                        }
                    });
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        file.delete();
    }
}
