package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

public class chattingList extends AppCompatActivity {

    TabLayout tabs;

    Fragment1 fragment1;
    FragmentTwo fragmentTwo;
    Fragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);
        LinearLayout chatlayout = (LinearLayout)findViewById(R.id.c_content);

        fragment1 = new Fragment1();
        fragmentTwo = new FragmentTwo();
        fragment3 = new Fragment3();

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("친구"));
        tabs.addTab(tabs.newTab().setText("채팅"));
        tabs.addTab(tabs.newTab().setText("검색"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0)
                    selected = fragment1;
                else if(position == 1){
                    selected = fragmentTwo;

                }


                else if(position == 2)
                    selected = fragment3;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}