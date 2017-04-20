package br.com.matteroftime.ui.userArea;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.selectMusic.SelectMusicDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAreaFragment extends Fragment implements UserAreaContract.View, OnMusicSelectedListener{


    private View view;
    private UserAreaAdapter adapter;
    private UserAreaContract.Actions presenter;
    private SelectMusicDialogFragment selectMusicDialogFragment;
    private Musica musica;

    @BindView(R.id.user_area_recycler_view) RecyclerView userAreaRecyclerView;
    @BindView(R.id.edtEmail) EditText email;
    @BindView(R.id.edtSenha) EditText senha;
    @BindView(R.id.txtMusicaEnvio) TextView musicaEnvio;
    @BindView(R.id.btnSelecionarMusica) Button selecionarMusica;
    @BindView(R.id.btnEnviarMusica) Button enviarMusica;
    @BindView(R.id.btnAtualizarMusica) Button atualizarMusica;
    @BindView(R.id.txtMusicaBaixar) TextView musicaBaixar;
    @BindView(R.id.edtMusicaPesquisar) EditText pesquisarMusica;
    @BindView(R.id.btnBaixarMusica) Button baixarMusica;



    public UserAreaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_area, container,false);
        musica = new Musica();
        ButterKnife.bind(this, view);
        presenter = new UserAreaPresenter(this);


        //setup Recyclerview
        List<Musica> tempMusicas = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new UserAreaAdapter(tempMusicas, getContext(), this);
        userAreaRecyclerView.setLayoutManager(layoutManager);
        userAreaRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onSelectMusic(Musica musicaSelecionada) {
        musica = musicaSelecionada;
        musicaBaixar.setText(musicaSelecionada.getNome());
    }

    @Override
    public void onLongClickMusic(Musica musicaClicada) {

    }

    @Override
    public void hideEmptyText() {

    }

    @Override
    public void showMusics(List<Musica> availableMusics) {

    }

    @Override
    public void showEmptyText() {

    }

    @Override
    public void showMessage(String error) {

    }
}
