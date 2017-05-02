package br.com.matteroftime.data.realm;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.ui.loginlogout.LoginLogoutContract;

/**
 * Created by RedBlood on 02/05/2017.
 */

public class LoginLogoutRepository implements LoginLogoutContract.Repository {
    @Override
    public void esqueciSenha(String mail, OnDatabaseOperationCompleteListener listener, Context context) {

    }

    @Override
    public void login(final String mail,final  String pass,final  Context context,final  OnDatabaseOperationCompleteListener listener) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", mail);
        jsonObject.addProperty("senha", pass);

        Ion.with(context)
                .load("http://matteroftime.com.br/login")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result.get("result").getAsString().equals("NO")){
                            //talvez usar resultado do exception -> e
                            listener.onSQLOperationFailed(context.getString(R.string.erro_login));
                        } else {
                            listener.onSQLOperationSucceded(context.getString(R.string.login_sucesso));
                        }
                    }
                });
    }
}
