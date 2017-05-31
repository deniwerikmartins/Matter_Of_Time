package br.com.matteroftime.ui.ajuda;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AjudaFragment extends Fragment {

    private View view;


    public AjudaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ajuda, container, false);
        ButterKnife.bind(this, view);
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);

        return view;
    }

}
