package com.cookandroid.ccit_suda;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    private Context context;
    ListView listView;
    followAdpater followAdpater;
    ArrayList<follow_list> follow_listArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = container.getContext();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        listView = (ListView)rootView.findViewById(R.id.ff_list);

        follow_listArrayList = new ArrayList<follow_list>();

        followAdpater = new followAdpater(context, follow_listArrayList);
        listView.setAdapter(followAdpater);
        return rootView;
    }
}
