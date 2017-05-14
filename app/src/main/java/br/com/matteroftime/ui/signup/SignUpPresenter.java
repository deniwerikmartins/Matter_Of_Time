package br.com.matteroftime.ui.signup;

import android.content.Context;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import javax.inject.Inject;

import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.util.Constants;

/**
 * Created by RedBlood on 02/05/2017.
 */

public class SignUpPresenter implements SignUpContract.Actions, OnDatabaseOperationCompleteListener {
    private final SignUpContract.View view;
    @Inject
    SignUpContract.Repository repository;


    public SignUpPresenter(SignUpContract.View view) {
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
    public void cadastraUsuario(String mail, String pass, Context context) {
        repository.cadastraUsuario(mail, pass, context, this);
    }


}

