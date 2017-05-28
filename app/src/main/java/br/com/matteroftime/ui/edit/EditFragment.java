package br.com.matteroftime.ui.edit;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.core.events.MusicListChangedEvent;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Compasso;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.addMusic.AddMusicDialogFragment;
import br.com.matteroftime.util.EventBus;
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
    private String[] valorNotas;
    private int nota;
    private boolean contagem;
    private Musica musica;
    @BindView(R.id.editList_recycler_view) RecyclerView editListRecyclerView;
    @BindView(R.id.empty_text) TextView emptyText;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.btnConfirmaCompasso) Button btnConfirmaCompasso;
    @BindView(R.id.btn_removerCompasso) Button btnRemoverCompasso;
    @BindView(R.id.btn_InserirCompasso) Button btnInserirCompasso;
    @BindView(R.id.spn_nota) Spinner spinner;
    @BindView(R.id.chk_pre_contagem) CheckBox checkBox;
    @BindView(R.id.txt_nome_musica) TextView nomeMusica;
    @BindView(R.id.txtNumeroMusica) TextView numeroMusica;
    @BindView(R.id.edt_ordem) EditText ordemDaMusica;
    @BindView(R.id.imgBtnConfirmaMusica) ImageButton confirmaMusica;
    @BindView(R.id.edt_contar) EditText contar;
    @BindView(R.id.txtNumCompasso) TextView txtNumeroCompasso;
    @BindView(R.id.txtTempoCompasso) TextView txtTempoCompasso;
    @BindView(R.id.txtNotaCompasso) TextView txtNotaCompasso;
    @BindView(R.id.txtBpmCompasso) TextView txtBpmCompasso;
    @BindView(R.id.txtRepeticoesCompasso) TextView txtRepeticoesCompasso;
    @BindView(R.id.edt_numero_compasso) EditText edtNumeroCompasso;
    @BindView(R.id.edt_bpm) EditText edtBpm;
    @BindView(R.id.edt_tempos) EditText edtTempos;
    @BindView(R.id.edt_repeticoes) EditText edtRepeticoes;
    @Inject Bus bus = new Bus();

    public EditFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_edit, container, false);
        musica = new Musica();
        ButterKnife.bind(this, view);
        valorNotas = new String[]{getString(R.string.semibreve),getString(R.string.minima),getString(R.string.seminima),getString(R.string.colcheia),getString(R.string.semicolcheia),getString(R.string.fusa),getString(R.string.semifusa)};
        bus.register(this);

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

        edtNumeroCompasso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !edtNumeroCompasso.getText().toString().isEmpty()){
                    int ord = Integer.parseInt(edtNumeroCompasso.getText().toString());
                    int x = 0;
                    if (musica == null){
                        showMessage(getString(R.string.sem_musica));
                    } else if (musica.getCompassos() == null || musica.getCompassos().size() == 0){
                        showMessage(getString(R.string.compasso_inexistente));
                    } else if (ord <= 0 || ord > musica.getCompassos().size()){
                        showMessage(getString(R.string.compasso_inexistente));
                    } else if(musica.getCompassos() != null && musica.getCompassos().size() > 0){
                        ord = ord - 1;
                        x = ord;
                        x = x + 1;
                        txtNumeroCompasso.setText(String.valueOf(x));
                        atualizaViewsCompasso(musica, musica.getCompassos().get(ord));
                    }
                }
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
    public void recebeMusica(Musica musica) {
        this.musica = musica;
        //atualizaNomeMusica(musica);
    }

    @Override
    public void showMusics(List<Musica> musicas) {
        adapter.replaceData(musicas);
    }

    @Override
    public void showAddMusicForm() {
        addMusicDialogFragment = AddMusicDialogFragment.newInstance(0);
        //Context context = getContext();
        addMusicDialogFragment.show(getActivity().getFragmentManager(), "Dialog");
        //addMusicDialogFragment.recebeContext(context);
    }

    @Override
    public void showEditMusicForm(Musica musica) {
        AddMusicDialogFragment dialog = AddMusicDialogFragment.newInstance(musica.getId());
        //Context context = getContext();
        dialog.show(getActivity().getFragmentManager(), "Dialog");
        //dialog.recebeContext(context);
    }

    @Override
    public void showDeleteMusicPrompt(final Musica musica) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = (View)inflater.inflate(R.layout.dialog_title,null);
        TextView titleText = (TextView) titleView.findViewById(R.id.txt_view_dialog_title);
        titleText.setText(getString(R.string.deletar_musica));
        alertDialog.setMessage(getString(R.string.deletar) + musica.getNome());
        alertDialog.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.deleteMusic(musica, getActivity().getBaseContext());
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
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
        //Snackbar.make(view.getRootView(),message, Snackbar.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    public void onSelectMusic(Musica musicaSelecionada) {
        presenter.ondAddToEditButtonClicked(musicaSelecionada);
        nomeMusica.setText(musicaSelecionada.getNome());
        numeroMusica.setText(String.valueOf(musica.getOrdem() + 1));
        //numeroMusica.setText(String.valueOf(musicaSelecionada.getOrdem() + 1));

    }

    @Override
    public void atualizaNomeMusica(Musica musicaSelecionada){
        nomeMusica.setText(musicaSelecionada.getNome());
        numeroMusica.setText(String.valueOf(musicaSelecionada.getOrdem() + 1));
    }

    @Override
    public void resetaNome() {
        nomeMusica.setText(getString(R.string.nomedamusica));
        numeroMusica.setText(String.valueOf(00));
    }

    @Override
    public void onLongClickMusic(Musica musicaClicada) {
        showMusicContextMenu(musicaClicada);
    }

    private void showMusicContextMenu(final Musica musicaClicada) {
        final String[] sortOptions = {getString(R.string.editar_musica), getString(R.string.deletar)};
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
        alertDialog.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
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
                        presenter.loadMusics();
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    @OnClick(R.id.chk_pre_contagem)
    public void onCheckboxClicked(View view){
        contagem = ((CheckBox) view).isChecked();
        if (contagem == false){
            contar.setActivated(false);
        } else {
            contar.setActivated(true);
        }
    }

    @OnClick(R.id.imgBtnConfirmaMusica)
    public void setConfirmaMusica(View view){
        int ord = 0;
        if (ordemDaMusica.getText().toString().isEmpty() && musica.isPossuiOrdem() == false){
            ordemDaMusica.setError(getString(R.string.obrigatorio));
            ordemDaMusica.requestFocus();
            showMessage(getString(R.string.sem_posicao));
        } else {
            if (!ordemDaMusica.getText().toString().isEmpty()){
                ord = Integer.parseInt(ordemDaMusica.getText().toString());
            } else {
                ord = musica.getOrdem();
            }
        }

        if (ord < 0 || ord > adapter.getItemCount()){
            showMessage(getString(R.string.posicao_invalida));
        } else if (musica.getNome() == null){
            showMessage(getString(R.string.sem_musica));
        } else if (ordemDaMusica.getText().toString().isEmpty() && musica.isPossuiOrdem() == false){
            ordemDaMusica.setError(getString(R.string.obrigatorio));
            ordemDaMusica.requestFocus();
            showMessage(getString(R.string.ordem_necessaria));
        } else if(presenter.getListaMusicas() == null){
            showMessage(getString(R.string.sem_musicas));
        } else if(contagem == true && contar.getText().toString().isEmpty() || contagem == true
                && Integer.parseInt(contar.getText().toString()) == 0) {
            showMessage(getString(R.string.sem_contagem));
        } else if (contagem == true) {
            if (ord - 1 < -1){
                showMessage(getString(R.string.posicao_invalida));

            } else if (ord > presenter.getListaMusicas().size()){
                showMessage(getString(R.string.posicao_invalida));
            }
            else{
                if (!ordemDaMusica.getText().toString().isEmpty()){
                    musica.setOrdem(Integer.parseInt(ordemDaMusica.getText().toString()) - 1);
                } else {
                    musica.setOrdem(ord);
                }

                musica.setPreContagem(contagem);
                musica.setTemposContagem(Integer.parseInt(contar.getText().toString()));
                musica.setPossuiOrdem(true);
                numeroMusica.setText(String.valueOf(musica.getOrdem() + 1));
                atualizaViewsMusica(musica);
                //bus.post(new MusicListChangedEvent());
                EventBus.getInstance().post(new MusicListChangedEvent());
                showMessage(getString(R.string.musica_confirmada));
            }
        } else if (contagem == false){
            if (ord - 1 < -1){
                showMessage(getString(R.string.musica_inexistente));
            } else if (ord > presenter.getListaMusicas().size()){
                showMessage(getString(R.string.musica_inexistente));
            } else {
                if (!ordemDaMusica.getText().toString().isEmpty()){
                    musica.setOrdem(Integer.parseInt(ordemDaMusica.getText().toString()) - 1);
                } else {
                    musica.setOrdem(ord);
                }
                musica.setPreContagem(contagem);
                musica.setPossuiOrdem(true);
                numeroMusica.setText(String.valueOf(musica.getOrdem() + 1));
                atualizaViewsMusica(musica);
                //bus.post(new MusicListChangedEvent());
                EventBus.getInstance().post(new MusicListChangedEvent());
                showMessage(getString(R.string.musica_confirmada));
            }
        }
    }

    @Override
    public void atualizaViewsMusica(Musica musica) {
        presenter.atualizaMusica(musica);
        List<Musica> musicas = presenter.getListaMusicas();
        //musicas.set(musica.getOrdem(), musica); //2
        boolean ordemIgual = possuiValoresRepetidos(musicas);
        if (ordemIgual == true){
            showMessage(getString(R.string.ordem_igual));
        }
        this.showMusics(musicas);
    }

    public boolean possuiValoresRepetidos(List<Musica> musicas){
        Set<Integer> conjunto = new TreeSet<>();
        for (Musica musica: musicas) {
            if (!conjunto.add(Integer.valueOf(musica.getOrdem()))){
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.btnConfirmaCompasso)
    public void setConfirmaCompasso(){
        int ord = 0;
        if (edtNumeroCompasso.getText().toString().isEmpty()){
            edtNumeroCompasso.setError(getString(R.string.obrigatorio));
            edtNumeroCompasso.requestFocus();
            showMessage(getString(R.string.sem_compasso));
        } else if (!edtNumeroCompasso.getText().toString().isEmpty()){
            ord = Integer.parseInt(edtNumeroCompasso.getText().toString());
        }
        if (musica.getOrdem() < 0 || musica.isPossuiOrdem() == false){
            ordemDaMusica.setError(getString(R.string.obrigatorio));
            ordemDaMusica.requestFocus();
            showMessage(getString(R.string.ordem_invalida));
        } else if (musica.getNome() == null){
            showMessage(getString(R.string.sem_musica));
        } else if (edtNumeroCompasso.getText().toString().isEmpty()){
            edtNumeroCompasso.setError(getString(R.string.obrigatorio));
            edtNumeroCompasso.requestFocus();
            showMessage(getString(R.string.sem_compasso));
        } else if (ord - 1 < 0){
            showMessage(getString(R.string.compasso_inexistente));
        } else if(ord > musica.getCompassos().size()){
            showMessage(getString(R.string.compasso_inexistente));
        } else if (edtBpm.getText().toString().isEmpty()){
            edtBpm.setError(getString(R.string.obrigatorio));
            edtBpm.requestFocus();
            showMessage(getString(R.string.bpm_necessario));

        } else if (edtTempos.getText().toString().isEmpty()){
            edtTempos.setError(getString(R.string.obrigatorio));
            edtTempos.requestFocus();
            showMessage(getString(R.string.tempo_necessario));
        } else if (edtRepeticoes.getText().toString().isEmpty()){
            edtRepeticoes.setError(getString(R.string.obrigatorio));
            edtRepeticoes.requestFocus();
            showMessage(getString(R.string.repeticoes_necessaria));
        } else if (!edtBpm.getText().toString().isEmpty()){
            int val = Integer.parseInt(edtBpm.getText().toString());
            if (val < 30 || val > 250){
                showMessage(getString(R.string.valor_incompativel));
            } else {
                Compasso compasso = new Compasso();
                compasso.setOrdem(Integer.parseInt(edtNumeroCompasso.getText().toString()));
                compasso.setBpm(Integer.parseInt(edtBpm.getText().toString()));
                compasso.setTempos(Integer.parseInt(edtTempos.getText().toString()));
                compasso.setNota(nota);
                compasso.setRepeticoes(Integer.parseInt(edtRepeticoes.getText().toString()));
            /*RealmList<Compasso> compassos = musica.getCompassos();
            compassos.set(compasso.getOrdem(), compasso);
            musica.setCompassos(compassos);*/
                //presenter.updateMusica(musica);
                presenter.atualizarCompassodaMusica(musica, compasso, getActivity().getBaseContext());
                //presenter.atualizarCompassodaMusica(musica, compasso);
                List<Musica> musicas = presenter.getListaMusicas();
                this.showMusics(musicas);
                recebeMusica(musicas.get(musica.getOrdem())); // -1?
                atualizaViewsCompasso(musica, compasso);
                bus.post(new MusicListChangedEvent());
            }
        }
    }

    @Override
    public void atualizaViewsCompasso(Musica musica, Compasso compasso) {
        txtTempoCompasso.setText(String.valueOf(compasso.getTempos()));
        txtNotaCompasso.setText(String.valueOf(compasso.getNota()));
        txtBpmCompasso.setText(String.valueOf(compasso.getBpm()));
        txtRepeticoesCompasso.setText(String.valueOf(compasso.getRepeticoes()));
    }

    @OnClick(R.id.btn_removerCompasso)
    public void setBtnRemoverCompasso(){
        int ord = -10;
        if (!edtNumeroCompasso.getText().toString().isEmpty()) {
            ord = Integer.parseInt(edtNumeroCompasso.getText().toString());
        }
        if (musica.getNome() == null) {
            showMessage(getString(R.string.sem_musica));
        } else if (ord == -10){
            showMessage(getString(R.string.sem_compasso));
        } else if (ord - 1 < 0 && ord - 1 > -10){
            showMessage(getString(R.string.compasso_inexistente));
        } else if(ord > musica.getCompassos().size()){
            showMessage(getString(R.string.compasso_inexistente));
        } else {
            musica.getCompassos().remove(ord - 1);
            presenter.atualizaMusica(musica);
            //presenter.updateMusica(musica);
            List<Musica> musicas = presenter.getListaMusicas();
            this.showMusics(musicas);
            showMessage(getString(R.string.tamanho_compassos) +  " " +String.valueOf(musica.getCompassos().size())+ " " + getString(R.string.compassos));
            bus.post(new MusicListChangedEvent());
        }
    }

    @OnClick(R.id.btn_InserirCompasso)
    public void setBtnInserirCompasso(){
        if(musica.getNome() == null){
            showMessage(getString(R.string.sem_musica));
        } else {
            Compasso compasso = new Compasso();
            compasso.setId(MatterOfTimeApplication.compassoPrimarykey.getAndIncrement());
            musica.getCompassos().add(compasso);
            presenter.atualizaMusica(musica);
            //presenter.updateMusica(musica);
            List<Musica> musicas = presenter.getListaMusicas();
            this.showMusics(musicas);
            showMessage(getString(R.string.tamanho_compassos) + " " +String.valueOf(musica.getCompassos().size())+ " " + getString(R.string.compassos));
            bus.post(new MusicListChangedEvent());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getInstance().unregister(this);
        super.onStop();


    }



    @Subscribe
    public void onMusicListChanged(MusicListChangedEvent event){
        presenter.loadMusics();
        List<Musica> musicas = presenter.getListaMusicas();
        adapter.replaceData(musicas);

    }
}
