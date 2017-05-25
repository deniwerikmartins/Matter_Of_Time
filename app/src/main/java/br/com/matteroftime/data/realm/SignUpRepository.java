package br.com.matteroftime.data.realm;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.ui.signup.SignUpContract;

/**
 * Created by RedBlood on 02/05/2017.
 */

public class SignUpRepository implements SignUpContract.Repository {

    @Override
    public void cadastraUsuario(final String mail, final String pass, final Context context
            , final OnDatabaseOperationCompleteListener listener) {
        Ion.with(context)
                .load("https://matteroftime-redblood666.c9users.io/registrar.php")
                .setBodyParameter("email", mail)
                .setBodyParameter("senha", pass)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result.get("result").getAsString().equals("EXISTENTE")){
                            listener.onSQLOperationFailed(context.getString(R.string.email_cadastrado));
                        } else if (result.get("result").getAsString().equals("NO")){
                            listener.onSQLOperationFailed(context.getString(R.string.erro_cadastrar));
                        } else {
                            listener.onSQLOperationSucceded(context.getString(R.string.cadastro_sucesso));
                        }
                    }
                });
    }
}
