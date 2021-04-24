package com.vientamthuong.eatsimple.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.vientamthuong.eatsimple.R;

import android.os.Bundle;

public class activity_login extends AppCompatActivity {

    TabLayout tapLayout;
    ViewPager viewPager;
    FloatingActionButton fb, gg, tw;
    float v =0;

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.table_layout)
//            return true;
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tapLayout.findViewById(R.id.table_layout1);
        viewPager.findViewById(R.id.view_paper1);
        fb.findViewById(R.id.fab_facebook);
        gg.findViewById(R.id.fab_google);
        tw.findViewById(R.id.fab_twitter);

        tapLayout.addTab(tapLayout.newTab().setText("Đăng nhập"));
        tapLayout.addTab(tapLayout.newTab().setText("Đăng kí"));
        tapLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this,tapLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tapLayout));



        fb.setTranslationY(300);
        gg.setTranslationY(300);
        tw.setTranslationY(300);
        tapLayout.setTranslationY(300);

        fb.setAlpha(v);
        gg.setAlpha(v);
        tw.setAlpha(v);
        tapLayout.setAlpha(v);

        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        gg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        tw.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tapLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

    }
}