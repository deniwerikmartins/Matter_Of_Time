package br.com.matteroftime.ui.selectMusic;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.matteroftime.R;

import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.util.Constants;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectMusicDialogFragment extends DialogFragment implements SelectMusicContract.View, OnMusicSelectedListener {


    private SelectMusicContract.Actions presenter;
    //private int t;
    private SelectMusicAdapter adapter;


    @BindView(R.id.select_music_recycler_view) RecyclerView selectMusicRecyclerView;
    @BindView(R.id.txt_sem_musicas) TextView semMusicas;



    public SelectMusicDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_music_dialog, container, false);
    }

    public static SelectMusicDialogFragment newInstance(long id) {
        SelectMusicDialogFragment fragment = new SelectMusicDialogFragment();
        if (id > 0){
            Bundle args = new Bundle();
            args.putLong(Constants.COLUMN_ID, id);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onSelectMusic(Musica musicaSelecionada) {

    }

    @Override
    public void onLongClickMusic(Musica musicaClicada) {

    }
}
