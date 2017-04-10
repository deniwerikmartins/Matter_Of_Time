package br.com.matteroftime.ui.play;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment implements PlayContract.View, OnMusicSelectedListener {

    private View view;
    private PlayAdapter playAdapter;
    private PlayContract.Actions presenter;
    private Integer[] valorNotas = new Integer[]{1,2,4,8,16,32,64};
    private int nota;
    private int select;


    @BindView(R.id.playlist_recycler_view) RecyclerView playlistRecyclerView;
    @BindView(R.id.edtTempos) EditText tempos;
    @BindView(R.id.spnNota) Spinner spinner;
    @BindView(R.id.edtBPM) EditText bpm;
    @BindView(R.id.txtBPMAtual) TextView bpmAtual;
    @BindView(R.id.txtTemposAtual) TextView temposAtual;
    @BindView(R.id.txtNotaAtual) TextView notaAtual;
    @BindView(R.id.txtProxBPM) TextView proxBpm;
    @BindView(R.id.txtProxTempos) TextView proxTempos;
    @BindView(R.id.txtProxNota) TextView proxNota;
    @BindView(R.id.btnOk) ImageButton imgBtnOk;
    @BindView(R.id.btnPlay) ImageButton imgBtnPlay;
    @BindView(R.id.btnStop) ImageButton imgBtnStop;
    @BindView(R.id.imgClick) ImageView imgClick;
    @BindView(R.id.imgStop) ImageView imgStop;

    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_play, container, false);

        ButterKnife.bind(this, view);
        presenter = new PlayPresenter(this);

        //setup RecyclerView
        List<Musica> tempMusicas = new ArrayList<>(); // ou RealmList
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        playAdapter = new PlayAdapter(tempMusicas, getContext(), this);
        playlistRecyclerView.setLayoutManager(layoutManager);
        playlistRecyclerView.setAdapter(playAdapter);


        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, valorNotas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner = (Spinner) spinner.findViewById(R.id.spn_notas);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nota = (int)spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onSelectMusic(Musica musicaSelecionada) {
        //musica recebida do listener
        presenter.defineMusica(musicaSelecionada);
    }

    @Override
    public void onLongClickMusic(Musica musicaClicada) {
        presenter.defineMusica(musicaClicada);
    }


    @Override
    public void mostrarMusicas(List<Musica> musicas) {
        playAdapter.replaceData(musicas);
    }

    @OnClick(R.id.btnOk)
    public void onClickOk(View view){
        int t = Integer.parseInt(tempos.getText().toString());
        int b = Integer.parseInt(bpm.getText().toString());
        presenter.criaCompasso(b, t, nota);
        bpmAtual.setText(String.valueOf(b));
        temposAtual.setText(String.valueOf(t));
        notaAtual.setText(String.valueOf(nota));
    }

    @OnClick(R.id.btnPlay)
    public void tocar(View view){
        presenter.play(getContext());
    }


    @OnClick(R.id.btnStop)
    public void parar(View view){
        presenter.stop();
    }





}
