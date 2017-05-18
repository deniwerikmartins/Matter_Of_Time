package br.com.matteroftime.ui.play;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import br.com.matteroftime.R;
import br.com.matteroftime.core.events.MusicListChangedEvent;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.matteroftime.R.string.compasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment implements PlayContract.View, OnMusicSelectedListener {

    private View view;
    private PlayAdapter playAdapter;
    private PlayContract.Actions presenter;
    private String[] valorNotas;
    private int nota;
    private String select;
    private Musica musica;
    Handler handler;

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
    @BindView(R.id.imgClick1) ImageView imgClick1;
    @BindView(R.id.imgClick2) ImageView imgClick2;
    @BindView(R.id.empty_text) TextView emptyText;
    @BindView(R.id.txtNomeMusica) TextView nomeMusica;
    @BindView(R.id.txtRepeticoesAtual) TextView repeticoesAtual;
    @BindView(R.id.txtRepeticoesTotal) TextView repeticoesTotal;
    @BindView(R.id.txtRepeticoesProximo) TextView repeticoesProximo;
    @BindView(R.id.txtRepeticoesCompassoAtual) TextView repeticoesCompassoAtual;
    @BindView(R.id.txtRepeticoesCompassoTotal) TextView repeticoesCompassoTotal;
    @Inject Bus bus = new Bus();

    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_play, container, false);
        valorNotas = new String[]{getString(R.string.semibreve),getString(R.string.minima),getString(R.string.seminima),getString(R.string.colcheia),getString(R.string.semicolcheia),getString(R.string.fusa),getString(R.string.semifusa)};
        ButterKnife.bind(this, view);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                Bundle data = msg.getData();
                switch (msg.what){
                    case 0:
                        if (imgClick1.getVisibility() == View.VISIBLE){
                            imgClick1.setVisibility(View.INVISIBLE);
                        }else{
                            imgClick1.setVisibility(View.VISIBLE);
                        }
                        if (imgClick1.getVisibility() == View.VISIBLE){
                            imgClick2.setVisibility(View.INVISIBLE);
                        } else {
                            imgClick2.setVisibility(View.VISIBLE);
                        }
                        String compassoAtualBPM = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_BPM));
                        String compassoAtualTempos = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_TEMPOS));
                        String compassoAtualNota = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_NOTA));
                        String compassoAtualTempoAtual = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_TEMPO_ATUAL));
                        String compassoAtualTempoTotal = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_TEMPO_TOTAL));

                        bpmAtual.setText(compassoAtualBPM);
                        temposAtual.setText(compassoAtualTempos);
                        notaAtual.setText(compassoAtualNota);
                        repeticoesAtual.setText(compassoAtualTempoAtual);
                        repeticoesTotal.setText(compassoAtualTempoTotal);
                        break;
                    case 1:
                        String compassoProximoBPM = String.valueOf(data.getInt(Constants.COMPASSO_PROXIMO_BPM));
                        String compassoProximoTempos = String.valueOf(data.getInt(Constants.COMPASSO_PROXIMO_TEMPOS));
                        String compassoProximoNota = String.valueOf(data.getInt(Constants.COMPASSO_PROXIMO_NOTA));
                        String compassoProximoRepeticoes = String.valueOf(data.getInt(Constants.COMPASSO_PROXIMO_REPETICOES));

                        proxBpm.setText(compassoProximoBPM);
                        proxTempos.setText(compassoProximoTempos);
                        proxNota.setText(compassoProximoNota);
                        repeticoesProximo.setText(compassoProximoRepeticoes);
                        break;
                    case 2:
                        String compassoRepeticoesAtual = String.valueOf(data.getInt(Constants.COMPASSO_REPETICOES_ATUAL));
                        String compassoRepeticoesTotal = String.valueOf(data.getInt(Constants.COMPASSO_REPETICOES_TOTAL));

                        repeticoesCompassoAtual.setText(compassoRepeticoesAtual);
                        repeticoesCompassoTotal.setText(compassoRepeticoesTotal);
                        break;
                    case 3:
                        proxBpm.setText(String.valueOf(msg.arg2));
                        proxTempos.setText(String.valueOf(msg.arg1));
                        proxNota.setText(String.valueOf(msg.arg1));
                        repeticoesProximo.setText(String.valueOf(msg.arg2));
                        break;
                    case 4:
                        repeticoesAtual.setText(String.valueOf(msg.arg1));
                        repeticoesTotal.setText(String.valueOf(msg.arg2));
                        break;
                    case 5:
                        String compassoAtualBPMCompasso = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_BPM));
                        String compassoAtualTempoTotalCompasso = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_TEMPO_TOTAL));
                        String compassoAtualNotaCompasso = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_NOTA));
                        String compassoAtualTempoAtualCompasso = String.valueOf(data.getInt(Constants.COMPASSO_ATUAL_TEMPO_ATUAL));

                        bpmAtual.setText(compassoAtualBPMCompasso);
                        temposAtual.setText(compassoAtualTempoTotalCompasso);
                        notaAtual.setText(compassoAtualNotaCompasso);
                        repeticoesAtual.setText(compassoAtualTempoAtualCompasso);
                        repeticoesTotal.setText(compassoAtualTempoTotalCompasso);
                        break;
                }
                //repeticoesAtual.setText(String.valueOf(msg.arg1));
            }
        };


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
                int selectedItemPosition = spinner.getSelectedItemPosition();
                switch (selectedItemPosition){
                    case 0:
                        nota = 1;
                        break;
                    case 1:
                        nota = 2;
                        break;
                    case 2:
                        nota = 4;
                        break;
                    case 3:
                        nota = 8;
                        break;
                    case 4:
                        nota = 16;
                        break;
                    case 5:
                        nota = 32;
                        break;
                    case 6:
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


        boolean ok = false;

        for (int i = 0; i < musicaSelecionada.getCompassos().size(); i++){
            if (musicaSelecionada.getCompassos().get(i).getTempos() == 0){
                showMessage(getString(R.string.o_compasso) + " " +String.valueOf(i+1)+ " " + getString(R.string.compassos_nao_definidos));
                ok = false;
                return;
            } else if(musicaSelecionada.getCompassos().get(i).getRepeticoes() == 0){
                showMessage(getString(R.string.o_compasso) + " " + String.valueOf(i+1)+ " "  + getString(R.string.repeticoes_nao_definidos));
                ok = false;
                return;
            }  else if(musicaSelecionada.getCompassos().get(i).getBpm() == 0){
                showMessage(getString(R.string.o_compasso) + " " +String.valueOf(i+1)+ " "  + getString(R.string.bpm_nao_definido));
                ok = false;
                return;
            } else if(musicaSelecionada.getCompassos().get(i).getOrdem() < 0) {
                showMessage(getString(R.string.o_compasso) + " " +String.valueOf(i+1)+ " "  + getString(R.string.ordem_nao_definida));
                ok = false;
                return;
            } else {
                ok = true;
            }
        }

        if (ok == true){
            musica = musicaSelecionada;
            presenter.defineMusica(musica);
            nomeMusica.setText(musica.getNome());
            repeticoesTotal.setText(String.valueOf(musica.getCompassos().get(0).getRepeticoes()));

            ListIterator listIterator = musica.getCompassos().listIterator();

            if (listIterator.hasNext()){
                listIterator.next();
                if (listIterator.hasNext()){
                    Compasso compasso = (Compasso) listIterator.next();
                    repeticoesProximo.setText(String.valueOf(compasso.getRepeticoes()));
                }
            }
            atualizaViewsMusica(0);
        }
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
            nomeMusica.setText(getString(compasso));
            bus.post(new MusicListChangedEvent());
            showMessage(getString(R.string.compasso_confirmado));
        }

    }

    @OnClick(R.id.btnPlay)
    public void tocar(View view){
        presenter.play(getContext(), handler);
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
