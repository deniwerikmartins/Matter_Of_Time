package br.com.matteroftime.ui.selectMusic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.matteroftime.R;

import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.userArea.UserAreaContract;
import br.com.matteroftime.ui.userArea.UserAreaFragment;
import br.com.matteroftime.ui.userArea.UserAreaPresenter;
import br.com.matteroftime.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectMusicDialogFragment extends DialogFragment implements SelectMusicContract.View, OnMusicSelectedListener {

    private View view;
    private SelectMusicAdapter adapter;
    private SelectMusicContract.Actions presenter;
    private Musica musica;



    @BindView(R.id.select_music_recycler_view) RecyclerView selectMusicRecyclerView;
    @BindView(R.id.txt_sem_musicas) TextView semMusicas;



    public SelectMusicDialogFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SelectMusicPresenter(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialogFragment = new AlertDialog.Builder(getActivity());
        if (savedInstanceState == null){
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View rootView = inflater.inflate(R.layout.fragment_select_music_dialog, null);
            dialogFragment.setView(rootView);
            ButterKnife.bind(this, rootView);

            if (getArguments() != null && getArguments().containsKey(Constants.COLUMN_ID)){
                presenter.checkStatus(getArguments().getLong(Constants.COLUMN_ID));
            }

            View titleView = inflater.inflate(R.layout.dialog_title,null);
            TextView titleText = (TextView)titleView.findViewById(R.id.txt_view_dialog_title);
            titleText.setText("Select Music");;
            dialogFragment.setCustomTitle(titleView);

            dialogFragment.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogFragment.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        List<Musica> tempMusicas = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        adapter = new SelectMusicAdapter(tempMusicas, getActivity().getBaseContext(), this);
        selectMusicRecyclerView.setLayoutManager(layoutManager);
        selectMusicRecyclerView.setAdapter(adapter);


        return dialogFragment.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadMusicas();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyText() {
        semMusicas.setVisibility(View.VISIBLE);
        selectMusicRecyclerView.setVisibility(View.GONE);
    }



    @Override
    public void hideEmptyText() {
        semMusicas.setVisibility(View.GONE);
        selectMusicRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();

        if (d != null){
            Button positiveButton = (Button)d.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //boolean readyToCloseDialog = false;
                    //mandar musica para o fragment
                    //testar com metodo que retorna a musica | muda retorno
                    dismiss();
                    /*readyToCloseDialog = true;
                    if (readyToCloseDialog){

                    }*/
                }

            });

            Button negativeButton = (Button)d.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    musica = null;
                    dismiss();
                }
            });
        }
    }

    @Override
    public void showMusicas(List<Musica> musicas){
        adapter.replaceData(musicas);
    }

    @Override
    public void onSelectMusic(Musica musicaSelecionada) {
        musica = musicaSelecionada;
    }

    @Override
    public void onLongClickMusic(Musica musicaClicada) {

    }
}
