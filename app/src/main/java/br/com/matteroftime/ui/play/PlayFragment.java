package br.com.matteroftime.ui.play;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.matteroftime.R;
import br.com.matteroftime.core.events.MusicListChangedEvent;
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
    //private Integer[] valorNotas = new Integer[]{1,2,4,8,16,32,64};
    private String[] valorNotas = new String[]{"Semibreve","Mímina","Seminima","Colcheia","Semicolcheia","Fusa","Semifusa"};
    private int nota;
    private String select;
    private Musica musica;


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
    @BindView(R.id.empty_text) TextView emptyText;
    @BindView(R.id.txtNomeMusica) TextView nomeMusica;
    @BindView(R.id.txtRepeticoesAtual) TextView repeticoesAtual;
    @BindView(R.id.txtRepeticoesTotal) TextView repeticoesTotal;
    @Inject Bus bus = new Bus();

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
        bus.register(this);

        //setup RecyclerView
        List<Musica> tempMusicas = new ArrayList<>(); // ou RealmList
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        playAdapter = new PlayAdapter(tempMusicas, getContext(), this);
        playlistRecyclerView.setLayoutManager(layoutManager);
        playlistRecyclerView.setAdapter(playAdapter);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, valorNotas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner = (Spinner) spinner.findViewById(R.id.spn_notas);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //nota = (int)spinner.getSelectedItem();
                select = (String)spinner.getSelectedItem();
                switch (select){
                                        case "Semibreve":
                                                nota = 1;
                                                break;
                                        case "Mímina":
                                                nota = 2;
                                                break;
                                        case "Seminima":
                                                nota = 4;
                                                break;
                                        case "Colcheia":
                                                nota = 8;
                                                break;
                                        case "Semicolcheia":
                                                nota = 16;
                                                break;
                                        case "Fusa":
                                                nota = 32;
                                                break;
                                        case "Semifusa":
                                                nota = 64;
                                                break;
                                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadMusics();
    }


    @Override
    public void mostrarMusicas(List<Musica> musicas) {
        playAdapter.replaceData(musicas);
    }

    @Override
    public void showEmptyText() {
        emptyText.setVisibility(View.VISIBLE);
        playlistRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyText() {
        emptyText.setVisibility(View.GONE);
        playlistRecyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public void onSelectMusic(Musica musicaSelecionada) {
        musica = musicaSelecionada;
        presenter.defineMusica(musicaSelecionada);
        nomeMusica.setText(musica.getNome());
        repeticoesTotal.setText(String.valueOf(musica.getCompassos().get(0).getRepeticoes()));
        atualizaViewsMusica(0);

    }

    @Override
    public void onLongClickMusic(Musica musicaClicada) {
        onSelectMusic(musicaClicada);
    }

    @OnClick(R.id.btnOk)
    public void onClickOk(View view){
        if (tempos.getText().toString().isEmpty()){
            tempos.setError(getString(R.string.obrigatorio));
            tempos.requestFocus();
            this.showMessage(getString(R.string.tempo_necessario));
        } else if (bpm.getText().toString().isEmpty()){
            bpm.setError(getString(R.string.obrigatorio));
            bpm.requestFocus();
            this.showMessage(getString(R.string.bpm_necessario));
        } else {
            int t = Integer.parseInt(tempos.getText().toString());
            int b = Integer.parseInt(bpm.getText().toString());
            presenter.criaCompasso(b, t, nota);
            bpmAtual.setText(String.valueOf(b));
            temposAtual.setText(String.valueOf(t));
            notaAtual.setText(String.valueOf(nota));
            nomeMusica.setText(getString(R.string.compasso));
            bus.post(new MusicListChangedEvent());
        }

    }

    @OnClick(R.id.btnPlay)
    public void tocar(View view){
        presenter.play(getContext());
    }


    @OnClick(R.id.btnStop)
    public void parar(View view){
        presenter.stop();
    }

    @Override
    public void atualizaViewsMusica(int i) {
        i = 0;
        if (musica.getCompassos().size() > 0){

            bpmAtual.setText(String.valueOf(musica.getCompassos().get(i).getBpm()));
            temposAtual.setText(String.valueOf(musica.getCompassos().get(i).getTempos()));
            notaAtual.setText(String.valueOf(musica.getCompassos().get(i).getNota()));

            if (musica.getCompassos().get(i+1) != null){
                proxBpm.setText(String.valueOf(musica.getCompassos().get(i+1).getBpm()));
                proxTempos.setText(String.valueOf(musica.getCompassos().get(i+1).getTempos()));
                proxNota.setText(String.valueOf(musica.getCompassos().get(i+1).getNota()));
            }
            bus.post(new MusicListChangedEvent());
        }

    }

    @Subscribe
    public void onMusicListChanged(MusicListChangedEvent event){
        presenter.loadMusics();
    }
}
