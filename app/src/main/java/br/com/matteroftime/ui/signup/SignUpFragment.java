package br.com.matteroftime.ui.signup;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements SignUpContract.View{

    private View view;
    private SignUpContract.Actions presenter;

    @BindView(R.id.edtEmail) TextView email;
    @BindView(R.id.edtSenha) TextView senha;
    @BindView(R.id.edtConfirmSenha) TextView confirmSenha;
    @BindView(R.id.btnCadastrar) Button cadastrar;
    @Inject Bus bus;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
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
        Snackbar.make(view.getRootView(),message, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnCadastrar)
    public void cadastar(View view){
        if (email.getText().toString().isEmpty()){
            showMessage(getString(R.string.email_necessario));
        } else if(senha.getText().toString().isEmpty()){
            showMessage(getString(R.string.senha_necessaria));
        } else if (confirmSenha.getText().toString().isEmpty()){
            showMessage(getString(R.string.confirmacaoSenhaNecessaria));
        } else if (!senha.getText().toString().equals(confirmSenha.getText().toString())){
            showMessage(getString(R.string.senha_nao_confere));
        } else {
            String mail = email.getText().toString();
            String pass = senha.getText().toString();
            Context context = getContext();
            presenter.cadastraUsuario(mail, pass, context);
        }


    }

}
