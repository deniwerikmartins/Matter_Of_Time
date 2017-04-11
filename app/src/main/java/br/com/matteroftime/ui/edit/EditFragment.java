package br.com.matteroftime.ui.edit;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.addMusic.AddMusicDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment implements EditContract.View, OnMusicSelectedListener{


    private View view;
    private EditAdapter adapter;
    private EditContract.Actions presenter;
    private AddMusicDialogFragment addMusicDialogFragment;
    private String[] valorNotas = new String[]{"Semibreve","Mímina","Seminima","Colcheia","Semicolcheia","Fusa","Semifusa"};
    private int nota;
    private String select;
    private boolean contagem;

    @BindView(R.id.editList_recycler_view) RecyclerView editListRecyclerView;
    @BindView(R.id.empty_text) TextView emptyText;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.btn_removerCompasso) Button btnRemoverCompasso;
    @BindView(R.id.btn_InserirCompasso) Button btnInserirCompasso;
    @BindView(R.id.spn_nota) Spinner spinner;
    @BindView(R.id.chk_pre_contagem) CheckBox checkBox;



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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddMusicButtonClicked();
            }
        });

        //Setup Recyclyerview
        List<Musica> tempMusicas = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new EditAdapter(tempMusicas, getContext(), this);
        editListRecyclerView.setLayoutManager(layoutManager);
        editListRecyclerView.setAdapter(adapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, valorNotas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
    public void showMusics(List<Musica> musicas) {
        adapter.replaceData(musicas);
    }

    @Override
    public void showAddMusicForm() {
        addMusicDialogFragment = AddMusicDialogFragment.newInstance(0);
        addMusicDialogFragment.show(getActivity().getFragmentManager(), "Dialog");
    }

    @Override
    public void showEditMusicForm(Musica musica) {
        AddMusicDialogFragment dialog = AddMusicDialogFragment.newInstance(musica.getId());
        dialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    @Override
    public void showDeleteMusicPrompt(final Musica musica) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View titleView = (View)inflater.inflate(R.layout.dialog_title,null);
        TextView titleText = (TextView) titleView.findViewById(R.id.txt_view_dialog_title);
        titleText.setText("Delete Music?");

        alertDialog.setMessage("Delete" + musica.getNome());
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.deleteMusic(musica);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    @Override
    public void showEmptyText() {
        emptyText.setVisibility(View.VISIBLE);
        editListRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyText() {
        emptyText.setVisibility(View.GONE);
        editListRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }

    private void showToastMessage(String message) {
        Snackbar.make(view.getRootView(),message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectMusic(Musica musicaSelecionada) {
        presenter.ondAddToEditButtonClicked(musicaSelecionada);
    }

    @Override
    public void onLongClickMusic(Musica musicaClicada) {
        showMusicContextMenu(musicaClicada);

    }

    private void showMusicContextMenu(final Musica musicaClicada) {
        final String[] sortOptions = {"Edit Music", "Delete", "Edit Measures"};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);

        View titleView = (View) inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView) titleView.findViewById(R.id.txt_view_dialog_title);
        if (musicaClicada.getNome() != null){
            titleText.setText(musicaClicada.getNome());
        }
        alertDialog.setCustomTitle(titleView);

        ListView dialogList = (ListView) convertView.findViewById(R.id.dialog_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1,sortOptions);
        dialogList.setAdapter(adapter);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final Dialog dialog = alertDialog.create();
        dialog.show();
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        presenter.onEditMusicaButtonClicked(musicaClicada);
                        dialog.dismiss();
                        break;
                    case 1:
                        presenter.onDeleteMusicButtonClicked(musicaClicada);
                        dialog.dismiss();
                        break;
                    case 2:
                        presenter.ondAddToEditButtonClicked(musicaClicada);
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    @OnClick(R.id.chk_pre_contagem)
    public void onCheckboxClicked(View view){
        contagem = ((CheckBox) view).isChecked();

    }
}
