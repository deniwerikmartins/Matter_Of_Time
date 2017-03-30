package br.com.matteroftime.ui.userArea;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.matteroftime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAreaFragment extends Fragment {


    public UserAreaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_area, container, false);
    }

}
