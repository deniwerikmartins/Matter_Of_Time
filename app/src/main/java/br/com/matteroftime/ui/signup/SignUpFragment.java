package br.com.matteroftime.ui.signup;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringDef;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.util.*;
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
        presenter = new SignUpPresenter(this);

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
        //Snackbar.make(view.getRootView(),message, Snackbar.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @OnClick(R.id.btnCadastrar)
    public void cadastar(View view){
        if (email.getText().toString().isEmpty()){
            email.setError(getString(R.string.obrigatorio));
            email.requestFocus();
            showMessage(getString(R.string.email_necessario));
        } else if(senha.getText().toString().isEmpty()){
            senha.setError(getString(R.string.obrigatorio));
            senha.requestFocus();
            showMessage(getString(R.string.senha_necessaria));
        } else if (confirmSenha.getText().toString().isEmpty()){
            confirmSenha.setError(getString(R.string.obrigatorio));
            confirmSenha.requestFocus();
            showMessage(getString(R.string.confirmacaoSenhaNecessaria));
        } else if (!senha.getText().toString().equals(confirmSenha.getText().toString())){
            showMessage(getString(R.string.senha_nao_confere));
        } else if(Constants.netWorkdisponibilidade(this.getActivity().getBaseContext()) == false) {
            showMessage(getString(R.string.sem_conexao));
        } else {
            String mail = email.getText().toString();

            //Stringp= "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\ .[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*| "(?:[\x01-\    x08 \x0b \x0c \x0e- \x1f \x21 \x23- \x5b \x5d- \x7f]|\\  [\ x01-\ x09\ x0b\ x0c\ x0e- \x7f])* ")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\ .)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?| \[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?) \.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\    x08 \x0b \x0c \x0e- \x1f \x21- \x5a \x53- \x7f]|  \\[ \x01- \x09 \x0b \x0c \x0e- \x7f])+)\])";
            //Pattern pattern = Pattern.compile("[a-zA-Z0-9]+[a-zA-Z0-9_.-]+@{1}[a-zA-Z0-9_.-]*\\.+[a-z]{2,4}");
            //Pattern pattern = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,4}\n");
            String p = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
            Pattern pattern = Pattern.compile(p);
            Matcher matcher = pattern.matcher(mail);
            boolean b = matcher.matches();
            if (b == true){
                String pass = senha.getText().toString();
                Context context = getActivity().getBaseContext();
                presenter.cadastraUsuario(mail, pass, context);
            } else {
                showMessage(getString(R.string.email_invalido));
            }


        }


    }



}
