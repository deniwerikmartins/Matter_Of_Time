package br.com.matteroftime.ui.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;

/**
 * Created by deni on 31/03/2017.
 */

public class Play extends AsyncTask<Musica,Bitmap,Void> {
    private Context context;
    private PlayContract.Actions pa;
    private long intervalo;

    public Play(Context context, PlayContract.Actions pa) {
        this.context = context;
        this.pa = pa;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Musica... params) {
        Timer timer = new Timer();
        Musica musica = params[0];
        //definir intervalos antes do play
        musica.defineIntervalo(musica.getCompassos());
        intervalo = (long) musica.getCompassos().get(0).getIntervalo();
        timer.schedule(new Play.beep(), 0, intervalo);
        return null;
    }

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public class beep extends TimerTask{

        @Override
        public void run() {
        //atualizar variavel intervalo
        }
    }
}
