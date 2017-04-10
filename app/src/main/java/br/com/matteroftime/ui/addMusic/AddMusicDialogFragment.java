package br.com.matteroftime.ui.addMusic;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.matteroftime.R;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AddMusicDialogFragment extends DialogFragment  implements AddMusicContract.View  {

    private AddMusicContract.Action presenter;
    private boolean editMode = false;

    @BindView(R.id.edt_nome_da_musica) EditText edtNomeDaMusica;
    @BindView(R.id.edt_quantidade_compassos) EditText edtQtdCompassos;

    public AddMusicDialogFragment() {
        // Required empty public constructor
    }

    public static AddMusicDialogFragment newInstance(long id) {
        AddMusicDialogFragment fragment = new AddMusicDialogFragment();
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
        presenter = new AddMusicPresenter(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogFragment = new AlertDialog.Builder(getActivity());
        if (savedInstanceState == null){
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.fragment_add_music, null);
            dialogFragment.setView(view);
            ButterKnife.bind(this, view);

            if (getArguments() != null && getArguments().containsKey(Constants.COLUMN_ID)){
                presenter.checkStatus(getArguments().getLong(Constants.COLUMN_ID));
            }

            View titleView = inflater.inflate(R.layout.dialog_title, null);
            TextView titleText = (TextView)titleView.findViewById(R.id.txt_view_dialog_title);
            titleText.setText(editMode ? "Update Music" : "Add Music");
            dialogFragment.setCustomTitle(titleView);

            dialogFragment.setPositiveButton(editMode ? "Update" : "Add", new DialogInterface.OnClickListener() {
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
        return dialogFragment.create();
    }

    @Override
    public void populateForm(Musica musica) {
        edtNomeDaMusica.setText(musica.getNome());
        edtQtdCompassos.setText(musica.getQtdCompassos());
    }

    @Override
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    @Override
    public void displayMessage(String message) {

    }

    private boolean validateInputs(){
        if (edtNomeDaMusica.getText().toString().isEmpty()){
            edtNomeDaMusica.setError("Name is required");
            edtNomeDaMusica.requestFocus();
            return false;
        }

        if (edtNomeDaMusica.getText().toString().isEmpty()){
            edtNomeDaMusica.setError("Quantity is required");
            edtQtdCompassos.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();

        if (d != null){
            Button positiveButton = (Button)d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean readyToCloseDialog = false;
                    if (validateInputs()){
                        saveMusic();
                        readyToCloseDialog = true;
                    }
                    if (readyToCloseDialog){
                        dismiss();
                    }
                }
            });
        }
    }

    public void saveMusic(){

        Musica musica = new Musica();
        musica.setNome(edtNomeDaMusica.getText().toString());
        musica.setQtdCompassos(Integer.parseInt(edtQtdCompassos.getText().toString()));

        presenter.ondAddMusicButtonClick(musica);
    }


}
