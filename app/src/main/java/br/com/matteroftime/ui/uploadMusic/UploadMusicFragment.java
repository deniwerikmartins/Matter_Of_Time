package br.com.matteroftime.ui.uploadMusic;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnDatabaseOperationCompleteListener;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.userArea.UserAreaFragment;
import br.com.matteroftime.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadMusicFragment extends DialogFragment implements UploadMusicContract.View{


    private View view;
    private View rootView;
    private UploadMusicContract.Action presenter;
    private Musica musica;
    private boolean editMode = false;
    private String email;
    private String senha;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.txtNomeMusicaEnvio) TextView nomeMusicaEnvio;


    public UploadMusicFragment() {
        // Required empty public constructor
    }

    public static UploadMusicFragment newInstance(long musicaId, long usuarioId){
        UploadMusicFragment fragment = new UploadMusicFragment();
        if (musicaId == 0){
            Bundle args = new Bundle();
            args.putLong(Constants.ID_USUARIO, usuarioId);
            fragment.setArguments(args);
        } else if (musicaId > 0){
            Bundle args = new Bundle();
            args.putLong(Constants.ID_MUSICA, musicaId); //COLUMN_ID
            args.putLong(Constants.ID_USUARIO, usuarioId);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UploadMusicPresenter(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogFragment = new AlertDialog.Builder(getActivity());

        if (savedInstanceState == null) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            rootView = inflater.inflate(R.layout.fragment_upload_music, null);
            dialogFragment.setView(rootView);
            ButterKnife.bind(this, rootView);

            if (getArguments() != null && getArguments().containsKey(Constants.ID_MUSICA)) {
                presenter.checkStatus(getArguments().getLong(Constants.ID_MUSICA));
            }

            View titleView = inflater.inflate(R.layout.dialog_title, null);
            TextView titleText = (TextView)titleView.findViewById(R.id.txt_view_dialog_title);
            titleText.setText(editMode ? getString(R.string.atualizar_musica) : getString(R.string.enviar));
            dialogFragment.setCustomTitle(titleView);

            dialogFragment.setPositiveButton(editMode ? getString(R.string.atualizar_musica) : getString(R.string.enviar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogFragment.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long id = sharedPreferences.getLong(Constants.ID_MUSICA, 0);
        if (id > 0){
            musica = presenter.getMusica(id);
            nomeMusicaEnvio.setText(musica.getNome());
        } else {
           showMessage(getString(R.string.sem_musica));
        }



        return dialogFragment.create();
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
                    Context context = getActivity().getBaseContext();

                    long usuarioId = sharedPreferences.getLong(Constants.ID_USUARIO, 0);
                    try{
                        presenter.enviaMusica(musica, context, usuarioId);
                        showMessage(getString(R.string.sucesso_envio));
                    } catch (Exception e){
                        showMessage(getString(R.string.erro_envio));
                    }


                    dismiss();
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
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;

    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }

    private void showToastMessage(String message) {
        Snackbar.make(rootView.getRootView(),message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean isEditMode() {
        return editMode;
    }
}
