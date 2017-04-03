package br.com.matteroftime.ui.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.matteroftime.R;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;

/**
 * Created by deni on 31/03/2017.
 */

public class Play extends AsyncTask<Musica,Void,Void> {

    private Context context;
    private PlayContract.Actions pa;
    private long intervalo;
    Musica musica;
    Timer timer = new Timer();
    int i;
    MediaPlayer clickForte = MediaPlayer.create(context, R.raw.clickforte);
    MediaPlayer clickFraco = MediaPlayer.create(context, R.raw.clickfraco);

    public Play() {
    }

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
        musica = params[0];
        for (i = 0;  i < musica.getCompassos().size(); i++){
            intervalo = (long) musica.getCompassos().get(i).getIntervalo();
            timer.schedule(new Play.click(), 0, intervalo);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }



    public class click extends TimerTask{

        private int tempoAtual = 1;

        @Override
        public void run() {

                for (int j = 0; j < musica.getCompassos().get(i).getRepeticoes(); j++){
                    /*if (tempoAtual == 0){
                        tempoAtual += 1;
                        //return;
                    }*/

                    if (tempoAtual == 1){
                        clickForte();
                    } else {
                        clickFraco();
                    }

                    if (tempoAtual == musica.getCompassos().get(i).getTempos()){
                        tempoAtual = 1;
                    }

                    tempoAtual++;
                }
        }
    }

    public void cancel() {
        timer.cancel();
    }

    public void clickFraco() {
        clickFraco.start();
    }

    public void clickForte() {
        clickForte.start();
    }
}
