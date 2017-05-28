package br.com.matteroftime.ui.play;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import br.com.matteroftime.R;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.util.Constants;

/**
 * Created by RedBlood on 14/05/2017.
 */

public class Play extends Thread implements Runnable{

    private Context context;
    private PlayContract.Actions pa;
    private long intervalo;
    Musica musica;
    Compasso compasso;
    Handler handler;
    Message message;
    Bundle data;
    ListIterator listIterator;

    public int tempoAtual;
    MediaPlayer clickForte;
    MediaPlayer clickFraco;


    public Play(Context context, PlayContract.Actions pa, Musica musica, Handler handler) {
        this.context = context;
        this.pa = pa;
        clickForte = MediaPlayer.create(context, R.raw.clickforte);
        clickFraco = MediaPlayer.create(context, R.raw.clickfraco);
        this.musica = musica;
        compasso = new Compasso();
        this.handler = handler;
        message = Message.obtain();
        data = new Bundle();
        listIterator = this.musica.getCompassos().listIterator();
        setPriority(MAX_PRIORITY);
    }

    @Override
    public void run() {
        super.run();

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        //COMPASSO
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
                        message = Message.obtain();
                        message.what = 5;
                        data = new Bundle();
                        data.putInt(Constants.COMPASSO_ATUAL_BPM, musica.getCompassos().get(0).getBpm());
                        data.putInt(Constants.COMPASSO_ATUAL_TEMPO_TOTAL, musica.getCompassos().get(0).getTempos());
                        data.putInt(Constants.COMPASSO_ATUAL_NOTA, musica.getCompassos().get(0).getNota());
                        data.putInt(Constants.COMPASSO_ATUAL_TEMPO_ATUAL, tempoAtual);
                        message.setData(data);
                        handler.sendMessage(message);
                        try {
                            sleep(intervalo);
                        } catch (InterruptedException e) {
                            return;
                            //e.printStackTrace();

                        }
                    } else {
                        clickFraco();
                        message = Message.obtain();
                        message.what = 5;
                        data.putInt(Constants.COMPASSO_ATUAL_BPM, musica.getCompassos().get(0).getBpm());
                        data.putInt(Constants.COMPASSO_ATUAL_TEMPO_TOTAL, musica.getCompassos().get(0).getTempos());
                        data.putInt(Constants.COMPASSO_ATUAL_NOTA, musica.getCompassos().get(0).getNota());
                        data.putInt(Constants.COMPASSO_ATUAL_TEMPO_ATUAL, tempoAtual);
                        message.setData(data);
                        handler.sendMessage(message);
                        try {
                            sleep(intervalo);
                        } catch (InterruptedException e) {
                            return;
                            //e.printStackTrace();

                        }
                    }
                }

            }

        }
        //PRÉ-PRÉ-CONTAGEM
        else if (musica.isCompasso() == false){

            long temposleep[] = new long[musica.getCompassos().size()];
            for (int i = 0; i < musica.getCompassos().size(); i++){
                temposleep[i] = (long) musica.getCompassos().get(i).getIntervalo();
            }
            //PRÉ-CONTAGEM
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
                        message = Message.obtain();
                        message.what = 4;
                        message.arg1 = tempoAtual;
                        message.arg2 = musica.getTemposContagem();
                        handler.sendMessage(message);
                        try {
                            sleep(intervalo);
                        } catch (InterruptedException e) {
                            return;
                            // e.printStackTrace();
                        }
                    } else {
                        clickFraco();
                        message = Message.obtain();
                        message.what = 4;
                        message.arg1 = tempoAtual;
                        message.arg2 = musica.getTemposContagem();
                        handler.sendMessage(message);
                        try {
                            sleep(intervalo);
                        } catch (InterruptedException e) {
                            return;
                            //e.printStackTrace();
                        }
                    }

                }
            }

            //MÚSICA
            for (int i = 0; i < musica.getCompassos().size(); i++){

                //Compasso compasso = new Compasso();
                if (listIterator.hasNext()){
                    compasso = (Compasso) listIterator.next();
                    if (listIterator.nextIndex() == i+1 && listIterator.hasNext()){
                        compasso = (Compasso) listIterator.next();
                    }
                }


                    message = Message.obtain();
                    message.what = 1;
                    data = new Bundle();
                    data.putInt(Constants.COMPASSO_PROXIMO_BPM, compasso.getBpm());
                    data.putInt(Constants.COMPASSO_PROXIMO_TEMPOS, compasso.getTempos());
                    data.putInt(Constants.COMPASSO_PROXIMO_NOTA, compasso.getNota());
                    data.putInt(Constants.COMPASSO_PROXIMO_REPETICOES, compasso.getRepeticoes());
                    message.setData(data);
                    handler.sendMessage(message);

                for (int j = 0; j < musica.getCompassos().get(i).getRepeticoes(); j++){
                    message = Message.obtain();
                    message.what = 2;
                    data = new Bundle();
                    data.putInt(Constants.COMPASSO_REPETICOES_ATUAL, j+1);
                    data.putInt(Constants.COMPASSO_REPETICOES_TOTAL, musica.getCompassos().get(i).getRepeticoes());
                    message.setData(data);
                    handler.sendMessage(message);

                    for (tempoAtual = 0; tempoAtual <= musica.getCompassos().get(i).getTempos(); tempoAtual++){
                        if (tempoAtual == 0){
                            tempoAtual += 1;
                            //return;
                        }

                        if (tempoAtual == 1){
                            clickForte();
                            message = Message.obtain();
                            message.what = 0;
                            data = new Bundle();
                            data.putInt(Constants.COMPASSO_ATUAL_BPM, musica.getCompassos().get(i).getBpm());
                            data.putInt(Constants.COMPASSO_ATUAL_TEMPOS, musica.getCompassos().get(i).getTempos());
                            data.putInt(Constants.COMPASSO_ATUAL_NOTA, musica.getCompassos().get(i).getNota());
                            data.putInt(Constants.COMPASSO_ATUAL_TEMPO_ATUAL, tempoAtual);
                            data.putInt(Constants.COMPASSO_ATUAL_TEMPO_TOTAL, musica.getCompassos().get(i).getTempos());
                            message.arg1 = tempoAtual;
                            message.setData(data);
                            handler.sendMessage(message);
                            try {
                                sleep(temposleep[i]);

                            } catch (InterruptedException e) {
                                return;
                                //e.printStackTrace();

                            }
                        } else {
                            clickFraco();
                            message = Message.obtain();
                            message.what = 0;
                            data = new Bundle();
                            data.putInt(Constants.COMPASSO_ATUAL_BPM, musica.getCompassos().get(i).getBpm());
                            data.putInt(Constants.COMPASSO_ATUAL_TEMPOS, musica.getCompassos().get(i).getTempos());
                            data.putInt(Constants.COMPASSO_ATUAL_NOTA, musica.getCompassos().get(i).getNota());
                            data.putInt(Constants.COMPASSO_ATUAL_TEMPO_ATUAL, tempoAtual);
                            data.putInt(Constants.COMPASSO_ATUAL_TEMPO_TOTAL, musica.getCompassos().get(i).getTempos());
                            message.arg1 = tempoAtual;
                            message.setData(data);
                            handler.sendMessage(message);
                            try {
                                sleep(temposleep[i]);
                            } catch (InterruptedException e) {
                                return;
                                //e.printStackTrace();

                            }
                        }
                    }
                }
            }
            message = Message.obtain();
            message.what = 3;
            message.arg1 = 0;
            message.arg2 = 000;
            handler.sendMessage(message);
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
