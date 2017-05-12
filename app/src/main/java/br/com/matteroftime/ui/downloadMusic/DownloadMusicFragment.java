package br.com.matteroftime.ui.downloadMusic;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment; //import que sendo v4 dá tilt
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.models.Musica;
import br.com.matteroftime.ui.uploadMusic.UploadMusicContract;
import br.com.matteroftime.ui.userArea.UserAreaAdapter;
import br.com.matteroftime.ui.userArea.UserAreaContract;
import br.com.matteroftime.ui.userArea.UserAreaFragment;
import br.com.matteroftime.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadMusicFragment extends DialogFragment implements DownloadMusicContract.View {

    private View rootView;
    private DownloadMusicContract.Action presenter;
    private static Musica musica;
    private static UserAreaAdapter adapter;
    private UserAreaContract.View userAreaView;

    @BindView(R.id.txtNomeMusicaBaixar) TextView nomeMusicaBaixar;


    public DownloadMusicFragment() {
        // Required empty public constructor
    }

    public static DownloadMusicFragment newInstance(Musica musicaSelecionada, UserAreaAdapter adapterUserArea){
        DownloadMusicFragment fragment = new DownloadMusicFragment();
        musica = musicaSelecionada;
        adapter = adapterUserArea;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DownloadMusicPresenter(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogFragment = new AlertDialog.Builder(getActivity());

        if (savedInstanceState == null) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            rootView = inflater.inflate(R.layout.fragment_download_music, null);
            dialogFragment.setView(rootView);
            ButterKnife.bind(this, rootView);

            View titleView = inflater.inflate(R.layout.dialog_title, null);
            TextView titleText = (TextView)titleView.findViewById(R.id.txt_view_dialog_title);
            titleText.setText(getString(R.string.baixar_musica));
            dialogFragment.setCustomTitle(titleView);

            dialogFragment.setPositiveButton(getString(R.string.baixar_musica), new DialogInterface.OnClickListener() {
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
            nomeMusicaBaixar.setText(musica.getNome());
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
                    try {
                        presenter.downloadMusica(musica, context);
                        userAreaView.showMessage(getString(R.string.sucesso_download));
                    } catch (Exception e){
                        userAreaView.showMessage(getString(R.string.erro_download));
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
    public void showMessage(String message) {
        showToastMessage(message);
    }

    @Override
    public void displayMessage(String message) {
        //Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_SHORT).show();
        userAreaView.showMessage(message);
    }

    private void showToastMessage(String message) {
        Snackbar.make(rootView.getRootView(),message, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void recebeUserAreaView(UserAreaFragment userAreaFragment) {
        this.userAreaView = userAreaFragment;

    }

    @Override
    public void showMusics(List<Musica> availableMusics) {
        adapter.replaceData(availableMusics);
    }
}
