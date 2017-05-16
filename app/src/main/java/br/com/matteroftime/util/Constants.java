package br.com.matteroftime.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class Constants {

    public final static int CADASTRO = 1;
    public final static int LOGINLOGOUT = 2;

    public final static String COLUMN_ID = "_id";
    public static final String EMAIL = "email";
    public static final String SENHA = "senha";
    public static final String ID_MUSICA = "id_musica";
    public static final String ID_USUARIO = "usuario_id";

    public static final String COMPASSO_ATUAL_BPM = "Compasso_Atual_BPM";
    public static final String COMPASSO_ATUAL_TEMPOS = "Compasso_Atual_Tempos";
    public static final String COMPASSO_ATUAL_NOTA = "Compasso_Atual_Nota";
    public static final String COMPASSO_ATUAL_TEMPO_ATUAL = "Compasso_Atual_Tempo_Atual";
    public static final String COMPASSO_ATUAL_TEMPO_TOTAL = "Compasso_Atual_Tempo_Total";

    public static final String COMPASSO_REPETICOES_ATUAL = "Compasso_Repeticoes_ATUAL";
    public static final String COMPASSO_REPETICOES_TOTAL = "Compasso_Repeticoes_Total";

    public static final String COMPASSO_PROXIMO_BPM = "Compasso_Proximo_BPM";
    public static final String COMPASSO_PROXIMO_TEMPOS = "Compasso_Proximo_Tempos";
    public static final String COMPASSO_PROXIMO_NOTA = "Compasso_Proximo_Nota";




    public static boolean netWorkdisponibilidade(Context cont){
        boolean conectado = false;
        ConnectivityManager conmag;
        conmag = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        conmag.getActiveNetworkInfo();
        //Verifica o WIFI
        if(conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
            conectado = true;
        }
        //Verifica o 3G
        else if(conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
            conectado = true;
        }
        else{
            conectado = false;
        }
        return conectado;
    }



}
