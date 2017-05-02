package br.com.matteroftime.ui.signup;

import android.content.Context;

import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;

/**
 * Created by RedBlood on 01/05/2017.
 */

public interface SignUpContract {

    public interface View{
        void showMessage(String message);
    }

    public interface Actions{

        void cadastraUsuario(String mail, String pass, Context context);
    }

    public interface Repository{

        void cadastraUsuario(String mail, String pass, Context context, OnDatabaseOperationCompleteListener listener);
    }

}
