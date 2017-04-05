package br.com.matteroftime.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.matteroftime.ui.edit.EditFragment;
import br.com.matteroftime.ui.play.PlayFragment;
import br.com.matteroftime.ui.userArea.UserAreaFragment;

/**
 * Created by deni on 05/04/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment selectedFragment;
        switch (position){
            case 0:
                selectedFragment = new PlayFragment();
                break;
            case 1:
                selectedFragment = new EditFragment();
                break;
            case 2:
                selectedFragment = new UserAreaFragment();
                break;
            default:
                selectedFragment = new PlayFragment();
                break;
        }
        return selectedFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Play";
                break;
            case 1:
                title = "Edit";
                break;
            case 2:
                title = "User Area";
                break;
        }
        return title;
    }
}
