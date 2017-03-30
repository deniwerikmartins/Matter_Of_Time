package br.com.matteroftime.ui.play;

import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.matteroftime.models.Compasso;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class PlayPresenter implements PlayContract.Actions{


    private int tempoAtual;

    Timer timer = new Timer().schedule(new PlayPresenter.click(), 0,300);





    @Override
    public List<Compasso> defineCompasso(final List<Compasso> compassos) {
        for (Compasso compasso : compassos) {
            switch (compasso.getNota()){
                //semi-breve
                case 1:
                    compasso.setIntervalo((1.0 / compasso.getBpm() * 4 ) * 60000.0);
                    break;
                //mínima
                case 2:
                    compasso.setIntervalo((1.0 / compasso.getBpm() * 2 ) * 60000.0);
                    break;
                //semínima
                case 4:
                    compasso.setIntervalo(1.0 / compasso.getBpm() * 60000.0);
                    break;
                //colcheia
                case 8:
                    compasso.setIntervalo((1.0 / compasso.getBpm() / 2 ) * 60000.0);
                    break;
                //semicolcheia
                case 16:
                    compasso.setIntervalo((1.0 / compasso.getBpm() / 4 ) * 60000.0);
                    break;
                //fusa
                case 32:
                    compasso.setIntervalo((1.0 / compasso.getBpm() / 8 ) * 60000.0);
                    break;
                //semifusa
                case 64:
                    compasso.setIntervalo((1.0 / compasso.getBpm() / 4 ) * 60000.0);
                    break;
            }
        }
        return compassos;
    }


    public class click extends TimerTask {

        @Override
        public void run() {

        }
    }




}
