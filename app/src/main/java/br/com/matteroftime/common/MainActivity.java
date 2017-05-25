package br.com.matteroftime.common;

import android.app.Activity;
import android.content.Intent;
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

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.squareup.otto.Bus;

import br.com.matteroftime.R;
import br.com.matteroftime.core.MatterOfTimeApplication;
import br.com.matteroftime.ui.loginlogout.LoginLogoutActivity;
import br.com.matteroftime.ui.signup.SignUpActivity;
import br.com.matteroftime.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Bus bus;
    private Activity activity;
    private AccountHeader header = null;
    private Drawer drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        bus = MatterOfTimeApplication.getInstance().getBus();
        MatterOfTimeApplication.getInstance().getAppComponent().inject(this);

        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.cadastro).withIcon(FontAwesome.Icon.faw_user_plus)
                                .withIdentifier(Constants.CADASTRO),
                        new PrimaryDrawerItem().withName(R.string.loginlogout).withIcon(FontAwesome.Icon.faw_sign_in)
                                .withIdentifier(Constants.LOGINLOGOUT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null && drawerItem instanceof Nameable){
                            onTouchDrawer(drawerItem.getIdentifier());

                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        setupViewPager();
    }

    private void onTouchDrawer(long identifier) {
        switch ((int) identifier){
            case Constants.CADASTRO:
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                break;
            case Constants.LOGINLOGOUT:
                startActivity(new Intent(MainActivity.this, LoginLogoutActivity.class));
                break;
        }
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
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
