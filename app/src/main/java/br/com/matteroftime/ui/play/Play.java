package br.com.matteroftime.ui.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.util.ArrayList;
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
    int k = 0;
    int i = 0;
    public int tempoAtual;
    MediaPlayer clickForte;
    MediaPlayer clickFraco;

    public Play(Context context, PlayContract.Actions pa) {
        this.context = context;
        this.pa = pa;
        clickForte = MediaPlayer.create(context, R.raw.clickforte);
        clickFraco = MediaPlayer.create(context, R.raw.clickfraco);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Musica... params) {
        musica = params[0];

        /*
        for ( k = 0; k < musica.getCompassos().size(); k++  ) {
            intervalo = (long)musica.getCompassos().get(k).getIntervalo();
            for (incrementador = 0;  k < musica.getCompassos().size(); incrementador++){
                //intervalo = (long) musica.getCompassos().get(incrementador).getIntervalo();
                timer.scheduleAtFixedRate(new Play.click(), 0, intervalo);
            }
        }
        */

        /*for (Compasso compasso : musica.getCompassos()) {
            intervalo = (long)compasso.getIntervalo();
            timer.scheduleAtFixedRate(new Play.click(),0,intervalo);
            //i++;
        }*/

        for (i = 0; i < musica.getCompassos().size(); i++){
            if (i < musica.getCompassos().size()){
                intervalo = (long)musica.getCompassos().get(i).getIntervalo();
                timer.scheduleAtFixedRate(new Play.click(),0,intervalo);
            }
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

        @Override
        public void run() {
                                                       //.get(j)
                for (int j = 0; j < musica.getCompassos().get(i).getRepeticoes(); j++){
                    //int x = i;
                    for (tempoAtual = 0; tempoAtual <= musica.getCompassos().get(i).getTempos(); tempoAtual++){
                        if (tempoAtual == 0){
                            tempoAtual += 1;
                            //return;
                        }

                        if (tempoAtual == 1){
                            clickForte();
                        } else {
                            clickFraco();
                        }

                        /*if (tempoAtual == musica.getCompassos().get(i).getTempos()){
                            tempoAtual = 1;
                        }*/

                        /*if (tempoAtual == musica.getCompassos().get(i).getTempos()
                                && musica.getCompassos().get(i).getId() == musica.getCompassos().size()
                                && j == musica.getCompassos().get(i).getRepeticoes() - 1){
                            timer.cancel();
                        }*/
                        //tempoAtual++;
                    }
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
