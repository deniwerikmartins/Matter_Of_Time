package br.com.matteroftime.ui.edit;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.addMusic.AddMusicDialogFragment;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment implements EditContract.View, OnMusicSelectedListener{


    private View view;
    private EditAdapter adapter;
    private EditContract.Actions presenter;
    private AddMusicDialogFragment addMusicDialogFragment;

    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view =  inflater.inflate(R.layout.fragment_edit, container, false);

        ButterKnife.bind(this, view);
        presenter = new EditPresenter(this);

        //Setup Recyclyerview

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadMusics();
    }

    @Override
    public void onSelectMusic(Musica musicaSelecionada) {
        presenter.ondAddToEditButtonClicked(musicaSelecionada);
    }

    @Override
    public void onLongClickMusic(Musica musicaClicada) {
        ///editar nome e compassos???
    }

    @Override
    public void showMusics(List<Musica> musicas) {
        adapter.replaceData(musicas);
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }

    private void showToastMessage(String message) {
        Snackbar.make(view.getRootView(),message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showAddMusicForm() {
        addMusicDialogFragment = AddMusicDialogFragment.newInstance(0);

    }

    @Override
    public void showEditMusicForm(Musica musica) {

    }

    @Override
    public void showDeleteMusicPrompt(Musica musica) {

    }

    @Override
    public void showEmptyText() {

    }

    @Override
    public void hideEmptyText() {

    }
}
