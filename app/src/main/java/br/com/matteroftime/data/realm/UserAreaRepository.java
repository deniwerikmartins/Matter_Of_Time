package br.com.matteroftime.data.realm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.userArea.UserAreaContract;
import br.com.matteroftime.ui.userArea.UserAreaPresenter;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by RedBlood on 20/04/2017.
 */

public class UserAreaRepository implements UserAreaContract.Repository {

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
    public List<Musica> pesquisaMusica(String nomeMusica, final OnDatabaseOperationCompleteListener listener, final Context context) {
        //final ArrayAdapter<JsonObject> musicasAd = null;
        final List<Musica> musicas = new ArrayList<>();
        //final Musica musica = null;
        JsonObject json = new JsonObject();
        json.addProperty("nome", nomeMusica);

        Ion.with(context)
                .load("http://matteroftime.com.br/pesquisar")
                //.setBodyParameter("nome", nomeMusica)
                .setJsonObjectBody(json)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result.size() > 0){
                            for (int i = 0; i < result.size(); i ++){
                                JsonObject jsonObject = result.get(i).getAsJsonObject();
                                //musicasAd.add(jsonObject);
                                /*musica.setNome(jsonObject.get("nome").getAsString());
                                musicas.add(musica);*/
                                musicas.add(new Musica());
                                musicas.get(i).setId(jsonObject.get("id").getAsLong());
                                musicas.get(i).setNome(jsonObject.get("nome").getAsString());
                            }
                            //listener.onSQLOperationSucceded(context.getString(R.string.ok));
                        } else {
                            listener.onSQLOperationFailed(context.getString(R.string.sem_resultados));
                        }
                    }
                });
        return musicas;
    }

    @Override
    public void baixaMusica(Musica musica, final OnDatabaseOperationCompleteListener listener, final Context context) {
        long id = musica.getId();
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        Future<File> downloading;

        downloading = Ion.with(context)
                .load("http://matteroftime.com.br/download")
                .setJsonObjectBody(json)
                .write(context.getFileStreamPath("archive_"+System.currentTimeMillis()+"_music.met"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File result) {
                        if (e != null){
                            listener.onSQLOperationFailed(context.getString(R.string.erro_download));
                            return;
                        } else {
                            listener.onSQLOperationSucceded(context.getString(R.string.sucesso_baixar));
                        }

                    }
                });

        Musica musica1 = new Musica();
        try {
            FileInputStream fileInputStream = new FileInputStream("data/data/br.com.matteroftime/"+musica.getNome()+"_music.met");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            musica1 = (Musica) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            listener.onSQLOperationSucceded(context.getString(R.string.sucesso_importar));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            listener.onSQLOperationFailed(context.getString(R.string.falha_importar));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onSQLOperationFailed(context.getString(R.string.falha_importar));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            listener.onSQLOperationFailed(context.getString(R.string.falha_importar));
        }


    }


}
