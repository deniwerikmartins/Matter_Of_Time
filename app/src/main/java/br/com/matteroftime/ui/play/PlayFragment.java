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
import android.widget.EditText;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.edt_tempos) EditText tempos;
    @BindView(R.id.spn_notas) Spinner spinner;
    @BindView(R.id.edt_BPM) EditText bpm;

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
        List<Musica> tempMusicas = new ArrayList<>();
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
                /*switch (select){
                    case 1:
                        nota = 1;
                        break;
                    case 2:
                        nota = 2;
                        break;
                    case 4:
                        nota = 4;
                        break;
                    case 8:
                        nota = 8;
                        break;
                    case 16:
                        nota = 16;
                        break;
                    case 32:
                        nota = 32;
                        break;
                    case 64:
                        nota = 64;
                        break;
                }*/
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
    public void mostrarMusicas(List<Musica> musicas) {
        playAdapter.replaceData(musicas);
    }

    @Override
    public void criaCompasso() {
        Compasso compasso = new Compasso();
        int t = Integer.parseInt(tempos.getText().toString());
        int b = Integer.parseInt(bpm.getText().toString());
        compasso.setTempos(t);
        compasso.setNota(nota);
        compasso.setBpm(b);
        List<Compasso> compassos = new ArrayList<>();
        compassos.set(0, compasso);
        Musica musica = new Musica();
        musica.setCompassos(compassos);
        musica.defineIntervalo(musica.getCompassos());
        presenter.defineMusica(musica);
    }

    @Override
    public void tocar() {
        presenter.play();
    }

    @Override
    public void parar() {
        presenter.stop();
    }
}
