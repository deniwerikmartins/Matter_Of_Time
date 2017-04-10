package br.com.matteroftime.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Bus;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private Bus bus;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activity = this;
        bus = MatterOfTimeApplication.getInstance().getBus();
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);
        setupViewPager();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            bus.register(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            bus.unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
