package br.com.matteroftime.ui.addMusic;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.matteroftime.R;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.util.Constants;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMusicDialogFragment extends DialogFragment implements AddMusicContract.View  {

    private AddMusicContract.Action presenter;
    private boolean editMode = false;


    //binds da view

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

            //View view = inflater.
            //dialogFragment.setView(view);
            //ButterKnife.bind(this, view);

            if (getArguments() != null && getArguments().containsKey(Constants.COLUMN_ID)){
                presenter.checkStatus(getArguments().getLong(Constants.COLUMN_ID));
            }

            //View titleView = inflater
            //TextView textText
            //titleText.set
            //dialogFragment.setCustomTitle(titleText);

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

    }

    @Override
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    @Override
    public void displayMessage(String message) {

    }

    private boolean validateInputs(){

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void saveMusic(){

    }
}
