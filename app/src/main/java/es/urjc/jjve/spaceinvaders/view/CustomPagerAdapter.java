package es.urjc.jjve.spaceinvaders.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import es.urjc.jjve.spaceinvaders.HighScoreActivity;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public CustomPagerAdapter(FragmentManager fm, List<Fragment> fragments) {

        super(fm);

        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {

        return this.fragments.get(position);

    }

    @Override
    public int getCount() {

        return this.fragments.size();

    }

}
