package br.com.matteroftime.ui.play;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.models.Musica;

/**
 * Created by RedBlood on 14/05/2017.
 */

public class Play extends Thread implements Runnable{

    private Context context;
    private PlayContract.Actions pa;
    private long intervalo;
    Musica musica;

    public int tempoAtual;
    MediaPlayer clickForte;
    MediaPlayer clickFraco;


    public Play(Context context, PlayContract.Actions pa, Musica musica) {
        this.context = context;
        this.pa = pa;
        clickForte = MediaPlayer.create(context, R.raw.clickforte);
        clickFraco = MediaPlayer.create(context, R.raw.clickfraco);
        this.musica = musica;
    }




    @Override
    public void run() {
        super.run();


        if (musica.isCompasso() == true){
            intervalo = (long)musica.getCompassos().get(0).getIntervalo();
            while (true){
                for (tempoAtual = 0; tempoAtual <= musica.getCompassos().get(0).getTempos();
                     tempoAtual ++){
                    if (tempoAtual == 0){
                        tempoAtual += 1;
                        //return;
                    }

                    if (tempoAtual == 1){
                        clickForte();
                        try {
                            sleep(intervalo);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clickFraco();
                        try {
                            sleep(intervalo);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        }

        else if (musica.isCompasso() == false){

            long temposleep[] = new long[musica.getCompassos().size()];
            for (int i = 0; i < musica.getCompassos().size(); i++){
                temposleep[i] = (long) musica.getCompassos().get(i).getIntervalo();
            }

            if (musica.isPreContagem() == true){
                intervalo = (long)musica.getCompassos().get(0).getIntervalo();
                for (tempoAtual = 0; tempoAtual <= musica.getTemposContagem();
                     tempoAtual ++){
                    if (tempoAtual == 0){
                        tempoAtual += 1;
                        //return;
                    }


                    if (tempoAtual == 1){
                        clickForte();
                        try {
                            sleep(intervalo);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clickFraco();
                        try {
                            sleep(intervalo);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            for (int i = 0; i < musica.getCompassos().size(); i++){
                for (int j = 0; j < musica.getCompassos().get(i).getRepeticoes(); j++){
                    for (tempoAtual = 0; tempoAtual <= musica.getCompassos().get(i).getTempos(); tempoAtual++){
                        if (tempoAtual == 0){
                            tempoAtual += 1;
                            //return;
                        }

                        if (tempoAtual == 1){
                            clickForte();
                            try {
                                sleep(temposleep[i]);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clickFraco();
                            try {
                                sleep(temposleep[i]);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }



    public void cancel(){
        this.interrupt();
    }

    public void clickFraco() {
        clickFraco.start();
    }

    public void clickForte() {
        clickForte.start();
    }
}
