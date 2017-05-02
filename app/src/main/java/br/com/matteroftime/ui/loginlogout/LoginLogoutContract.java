package br.com.matteroftime.ui.loginlogout;

import android.content.Context;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;

/**
 * Created by RedBlood on 01/05/2017.
 */

public interface LoginLogoutContract {

    public interface View{
        void showMessage(String message);
    }

    public interface Actions{

        void esqueciSenha(String mail, Context context);

        void login(String mail, String pass, Context context);
    }

    public interface Repository{

        void esqueciSenha(String mail, OnDatabaseOperationCompleteListener listener, Context context);

        void login(String mail, String pass, Context context, OnDatabaseOperationCompleteListener listener);
    }
}
