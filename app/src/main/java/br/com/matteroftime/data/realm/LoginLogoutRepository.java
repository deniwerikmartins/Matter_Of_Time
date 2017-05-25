package br.com.matteroftime.data.realm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.ui.loginlogout.LoginLogoutContract;
import br.com.matteroftime.util.Constants;

/**
 * Created by RedBlood on 02/05/2017.
 */

public class LoginLogoutRepository implements LoginLogoutContract.Repository {
    long id;
    private SharedPreferences sharedPreferences;

    @Override
    public void esqueciSenha(String mail, OnDatabaseOperationCompleteListener listener
            , Context context) {

    }

    @Override
    public void login(final String mail, final  String pass, final  Context context
            , final  OnDatabaseOperationCompleteListener listener) {

        Ion.with(context)
                .load("https://matteroftime-redblood666.c9users.io/login.php")
                .setBodyParameter("email", mail)
                .setBodyParameter("senha", pass)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result.get("result").getAsString().equals("NO")){
                            if (result.get("erro").getAsString().equals("email")){
                                //listener.onSQLOperationFailed(context.getString(R.string.erro_login));
                                listener.onSQLOperationFailed(context.getString(R.string.email_invalido));
                            } else if(result.get("erro").getAsString().equals("senha")){
                                listener.onSQLOperationFailed(context.getString(R.string.senha_invalida));
                            }
                        } else {
                            id = result.get("id").getAsLong();
                            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong(Constants.ID_USUARIO, id).commit();
                            listener.onSQLOperationSucceded(context.getString(R.string.login_sucesso));
                        }
                    }
                });
    }
}
