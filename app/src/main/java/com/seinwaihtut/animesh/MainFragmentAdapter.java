package com.seinwaihtut.animesh;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFragmentAdapter extends FragmentStateAdapter {
    ArrayList<Object> fragments;

    public MainFragmentAdapter(@NonNull Fragment fragment, ArrayList<Object> fragments) {
        super(fragment);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return (Fragment) fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
