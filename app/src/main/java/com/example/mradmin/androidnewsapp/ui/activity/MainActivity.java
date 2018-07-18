package com.example.mradmin.androidnewsapp.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mradmin.androidnewsapp.R;
import com.example.mradmin.androidnewsapp.ui.fragment.MainFragment;
import com.example.mradmin.androidnewsapp.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String URL_LIFE = "https://life.ru/xml/feed.xml";
    public static final String URL_NY_TIMES = "https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml";
    public static final String URL_AL_JAZEERA = "https://www.aljazeera.com/xml/rss/all.xml";
    public static final String URL_RT = "https://www.rt.com/rss/news/";

    private Drawable[] logos;
    private String[] titles;

    @BindView(R.id.content)
    FrameLayout frameLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_toolbar_logo)
    CircleImageView ivToolbarLogo;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.ib_refresh_info)
    ImageButton ibRefreshInfo;
    @BindView(R.id.navigation)
    BottomNavigationView navigationView;

    private static int navSelector = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_one:
                    //mTextMessage.setText(R.string.title_home);
                    navSelector = 0;
                    initNewsSelector();
                    return true;
                case R.id.navigation_two:
                    //mTextMessage.setText(R.string.title_dashboard);
                    navSelector = 1;
                    initNewsSelector();
                    return true;
                case R.id.navigation_three:
                    //mTextMessage.setText(R.string.title_dashboard);
                    navSelector = 2;
                    initNewsSelector();
                    return true;
                case R.id.navigation_four:
                    //mTextMessage.setText(R.string.title_dashboard);
                    navSelector = 3;
                    initNewsSelector();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        logos = new Drawable[]{getDrawable(R.drawable.life_logo), getDrawable(R.drawable.nytimes_logo), getDrawable(R.drawable.aljazeera_logo), getDrawable(R.drawable.rt_logo)};
        titles = new String[]{"Life.ru", "New York Times", "Al Jazeera", "RT"};

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navSelector = 0;
        initNewsSelector();

    }

    private void initFragment(Fragment fragment, String url) {

        initToolbar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
    }

    private void initToolbar() {
        ivToolbarLogo.setImageDrawable(logos[navSelector]);
        tvToolbarTitle.setText(titles[navSelector]);
    }

    private void initNewsSelector() {
        switch (navSelector) {
            case 0: {
                initFragment(MainFragment.newInstance(URL_LIFE, titles[navSelector]), URL_LIFE);
                break;
            }
            case 1: {
                initFragment(MainFragment.newInstance(URL_NY_TIMES, titles[navSelector]), URL_NY_TIMES);
                break;
            }
            case 2: {
                initFragment(MainFragment.newInstance(URL_AL_JAZEERA, titles[navSelector]), URL_AL_JAZEERA);
                break;
            }
            case 3: {
                initFragment(MainFragment.newInstance(URL_RT, titles[navSelector]), URL_RT);
            }
        }
    }

    @OnClick(R.id.ib_refresh_info)
    void onClickRefreshInfo() {
        Util.showAnchoredPopUpView(this, ibRefreshInfo, R.layout.refresh_info_popup, R.string.popup_refresh_info);
    }
}
