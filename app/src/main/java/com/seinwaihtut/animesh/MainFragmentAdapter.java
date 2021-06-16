package com.seinwaihtut.animesh;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.seinwaihtut.animesh.Airing.SeasonFragment;
import com.seinwaihtut.animesh.Watching.WatchingFragment;

import java.util.HashMap;
import java.util.Map;

public class MainFragmentAdapter extends FragmentStateAdapter {
    public MainFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment seasonFragment = new SeasonFragment();
        Fragment watchingFragment = new WatchingFragment();

        Map<Integer, Fragment> fragmentMap = new HashMap<>();
        fragmentMap.put(0, seasonFragment);
        fragmentMap.put(1, watchingFragment);

        return fragmentMap.get(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
