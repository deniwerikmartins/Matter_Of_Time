package br.com.matteroftime.ui.loginlogout;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginLogoutFragment extends Fragment implements LoginLogoutContract.View{

    private View view;
    private LoginLogoutContract.Actions presenter;
    @BindView(R.id.edtEmail) TextView email;
    @BindView(R.id.edtSenha) TextView senha;
    @BindView(R.id.btnEntrar) Button entrar;
    @BindView(R.id.btnSair) Button sair;
    @BindView(R.id.btnEsqueciSenha) Button esqueciSenha;
    @Inject Bus bus;
    private Context context;

    public LoginLogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenter = new LoginLogoutPresenter(this);
        view = inflater.inflate(R.layout.fragment_login_logout, container, false);
        ButterKnife.bind(this, view);
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        context = getContext();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //presenter.
        try {
            bus.register(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            bus.unregister(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }

    private void showToastMessage(String message) {
        //Snackbar.make(view.getRootView(),message, Snackbar.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @OnClick(R.id.btnEntrar)
    public void entrar(View view){
        if (email.getText().toString().isEmpty()){
            email.setError(getString(R.string.obrigatorio));
            email.requestFocus();
            showMessage(getString(R.string.email_necessario));
        } else if (senha.getText().toString().isEmpty()){
            senha.setError(getString(R.string.obrigatorio));
            senha.requestFocus();
            showMessage(getString(R.string.senha_necessaria));
        } else if(Constants.netWorkdisponibilidade(this.getActivity().getBaseContext()) == false) {
            showMessage(getString(R.string.sem_conexao));
        } else {
            String mail = email.getText().toString();
            String pass = senha.getText().toString();

            presenter.login(mail, pass, context);
        }

    }



    @OnClick(R.id.btnSair)
    public void sair(View view){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long id = sharedPreferences.getLong(Constants.ID_USUARIO,0);
        if (id > 0){
            editor.putLong(Constants.ID_USUARIO, 0).commit();
            showMessage(getString(R.string.logout_sucesso));
        } else {
            showMessage(getString(R.string.usuario_nao_logado));
        }


    }

    @OnClick(R.id.btnEsqueciSenha)
    public void esqueciSenha(View view){
        if (email.getText().toString().isEmpty()){
            email.setError(getString(R.string.obrigatorio));
            email.requestFocus();
            showMessage(getString(R.string.email_necessario));
        } else {
            String mail = email.getText().toString();
            presenter.esqueciSenha(mail, context);
        }
    }
}
