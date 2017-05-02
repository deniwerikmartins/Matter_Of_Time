package br.com.matteroftime.ui.loginlogout;

import android.content.Context;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;

/**
 * Created by RedBlood on 02/05/2017.
 */

public class LoginLogoutPresenter implements LoginLogoutContract.Actions, OnDatabaseOperationCompleteListener{
    private final LoginLogoutContract.View view;
    @Inject LoginLogoutContract.Repository repository;

    public LoginLogoutPresenter(LoginLogoutContract.View view) {
        this.view = view;
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void onSQLOperationFailed(String error) {
        view.showMessage(error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        view.showMessage(message);
    }

    @Override
    public void login(String mail, String pass, Context context) {
        repository.login(mail, pass, context, this);
    }

    @Override
    public void esqueciSenha(String mail, Context context) {
        repository.esqueciSenha(mail, this, context);
    }
}
