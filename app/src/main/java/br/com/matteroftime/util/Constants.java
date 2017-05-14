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
